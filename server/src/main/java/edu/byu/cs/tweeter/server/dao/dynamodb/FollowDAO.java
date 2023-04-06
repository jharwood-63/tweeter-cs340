package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.FollowBean;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAO extends DAOUtils implements IFollowDAO {
    private static final String FOLLOWS_TABLE_NAME = "follows";
    private static final String FOLLOWS_INDEX_NAME = "follow_index";

    private static final String FOLLOWS_PARTITION_KEY = "follow_handle";
    private static final String FOLLOWS_SORT_KEY = "followee_handle";

    private static final int PAGE_SIZE = 20;

    private DynamoDbTable<FollowBean> followTable;
    private DynamoDbIndex<FollowBean> followIndex;

    private DynamoDbTable<FollowBean> getFollowTable() {
        if (followTable == null) {
            followTable = getEnhancedClient().table(FOLLOWS_TABLE_NAME, TableSchema.fromBean(FollowBean.class));
        }

        return followTable;
    }

    private DynamoDbIndex<FollowBean> getFollowIndex() {
        if (followIndex == null) {
            followIndex = getEnhancedClient().table(FOLLOWS_TABLE_NAME, TableSchema.fromBean(FollowBean.class)).index(FOLLOWS_INDEX_NAME);
        }

        return followIndex;
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public GetFollowingResponse getFollowing(GetFollowingRequest request) {
        QueryEnhancedRequest queryRequest = createPagedQueryRequest(request.getFollowerAlias(), request.getLimit(),
                request.getLastFolloweeAlias(), FOLLOWS_PARTITION_KEY, FOLLOWS_SORT_KEY);

        GetFollowingResponse response = new GetFollowingResponse(new ArrayList<>(), false);

        PageIterable<FollowBean> pages = getFollowTable().query(queryRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowBean> page) -> {
                    response.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(followee -> response.getFollowees().add(followee.convertFollowerToUser()));
                });

        return response;
    }

    /*
    differences: table vs index, way information is retrieved
     */

    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
        QueryEnhancedRequest queryRequest = createPagedQueryRequest(request.getFolloweeAlias(), request.getLimit(),
                request.getLastFollowerAlias(), FOLLOWS_SORT_KEY, FOLLOWS_PARTITION_KEY);

        GetFollowersResponse response = new GetFollowersResponse(new ArrayList<>(), false);

        SdkIterable<Page<FollowBean>> sdkIterable = getFollowIndex().query(queryRequest);
        PageIterable<FollowBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowBean> page) -> {
                    response.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(followBean -> response.getFollowers().add(followBean.convertFolloweeToUser()));
                });

        return response;
    }

    @Override
    public List<User> getAllFollowers(String followeeAlias) {
        boolean hasMorePages = true;
        String lastFollowerAlias = null;
        List<User> followers = new ArrayList<>();

        while (hasMorePages) {
            AtomicBoolean more = new AtomicBoolean(false);
            QueryEnhancedRequest queryRequest = createPagedQueryRequest(followeeAlias, PAGE_SIZE,
                    lastFollowerAlias, FOLLOWS_SORT_KEY, FOLLOWS_PARTITION_KEY);

            SdkIterable<Page<FollowBean>> sdkIterable = getFollowIndex().query(queryRequest);
            PageIterable<FollowBean> pages = PageIterable.create(sdkIterable);
            pages.stream()
                    .limit(1)
                    .forEach((Page<FollowBean> page) -> {
                        more.set(page.lastEvaluatedKey() != null);
                        page.items().forEach(followBean -> followers.add(followBean.convertFolloweeToUser()));
                    });

            if (followers.size() > 0) {
                lastFollowerAlias = followers.get(followers.size() - 1).getAlias();
            }

            hasMorePages = more.get();
        }

        return followers;
    }

    private QueryEnhancedRequest createPagedQueryRequest(String targetUserAlias, int pageSize, String lastUserAlias, String partitionKey, String sortKey) {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if (isNonEmptyString(lastUserAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(partitionKey, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(sortKey, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        return requestBuilder.build();
    }

    @Override
    public void unfollow(UnfollowRequest request) {
        Key key = Key.builder()
                .partitionValue(request.getFollower().getAlias())
                .sortValue(request.getFollowee().getAlias())
                .build();

        getFollowTable().deleteItem(key);
    }

    @Override
    public void follow(FollowRequest request) {
        FollowBean followBean = new FollowBean();
        followBean.setFollow_handle(request.getFollower().getAlias());
        followBean.setFollow_name(request.getFollower().getName());
        followBean.setFollow_image(request.getFollower().getImageUrl());
        followBean.setFollowee_handle(request.getFollowee().getAlias());
        followBean.setFollowee_name(request.getFollowee().getName());
        followBean.setFollowee_image(request.getFollowee().getImageUrl());

        getFollowTable().putItem(followBean);
    }

    // am I following them?
    @Override
    public boolean isFollower(IsFollowerRequest request) {
        Key key = Key.builder()
                .partitionValue(request.getFollowerAlias())
                .sortValue(request.getFolloweeAlias())
                .build();

        FollowBean follow = getFollowTable().getItem(key);

        return follow != null;
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
}
