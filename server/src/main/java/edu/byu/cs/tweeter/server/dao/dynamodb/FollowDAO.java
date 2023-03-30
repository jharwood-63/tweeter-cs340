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
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.FollowBean;
import edu.byu.cs.tweeter.util.FakeData;
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

    private static final int PAGE_SIZE = 5;

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

    public int getFollowingCount(String userAlias) {
        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert user != null;
        return getFakeData().getFakeUsers().size();
    }

    public int getFollowersCount(String userAlias) {
        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert user != null;
        return getFakeData().getFakeUsers().size();
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
                request.getLastFolloweeAlias(), FOLLOWS_SORT_KEY, FOLLOWS_PARTITION_KEY);

        GetFollowingResponse response = new GetFollowingResponse(new ArrayList<>(), false);

        SdkIterable<Page<FollowBean>> sdkIterable = getFollowIndex().query(queryRequest);
        PageIterable<FollowBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowBean> page) -> {
                    response.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(followBean -> response.getFollowees().add(followBean.convertFolloweeToUser()));
                });

        if (response.getFollowees().size() == 0) {
            return new GetFollowingResponse("Unable to retrieve following with the given information");
        }

        return response;
    }

    /*
    differences: table vs index, way information is retrieved
     */

    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
        QueryEnhancedRequest queryRequest = createPagedQueryRequest(request.getFolloweeAlias(), request.getLimit(),
                request.getLastFollowerAlias(), FOLLOWS_PARTITION_KEY, FOLLOWS_SORT_KEY);

        GetFollowersResponse response = new GetFollowersResponse(new ArrayList<>(), false);

        PageIterable<FollowBean> pages = getFollowTable().query(queryRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowBean> page) -> {
                    response.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(followBean -> response.getFollowers().add(followBean.convertFollowerToUser()));
                });

        if (response.getFollowers().size() == 0) {
            return new GetFollowersResponse("Unable to retrieve followers with the given information");
        }

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
                    lastFollowerAlias, FOLLOWS_PARTITION_KEY, FOLLOWS_SORT_KEY);

            PageIterable<FollowBean> pages = getFollowTable().query(queryRequest);
            pages.stream()
                    .limit(1)
                    .forEach((Page<FollowBean> page) -> {
                        more.set(page.lastEvaluatedKey() != null);
                        page.items().forEach(followBean -> followers.add(followBean.convertFollowerToUser()));
                    });

            if (followers.size() > 0) {
                lastFollowerAlias = followers.get(followers.size() - 1).getAlias();
                hasMorePages = more.get();
            }
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
    public UnfollowResponse unfollow(UnfollowRequest request) {
        return null;
    }

    @Override
    public FollowResponse follow(FollowRequest request) {
        return null;
    }

    @Override
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        return null;
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollows the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getStartingIndex(String lastAlias, List<User> allFollows) {

        int index = 0;

        if(lastAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollows.size(); i++) {
                if(lastAlias.equals(allFollows.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    index = i + 1;
                    break;
                }
            }
        }

        return index;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }

    List<User> getDummyFollowers() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
}
