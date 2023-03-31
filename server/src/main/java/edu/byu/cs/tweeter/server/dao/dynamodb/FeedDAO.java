package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.server.dao.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.FeedBean;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.FollowBean;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class FeedDAO extends DAOUtils implements IFeedDAO {
    private static final String FEED_TABLE_NAME = "feed";
    private static final String FEED_PARTITION_KEY = "receiverAlias";
    private static final String FEED_SORT_KEY = "timestamp";

    private DynamoDbTable<FeedBean> feedTable;

    private DynamoDbTable<FeedBean> getFeedTable() {
        if (feedTable == null) {
            feedTable = getEnhancedClient().table(FEED_TABLE_NAME, TableSchema.fromBean(FeedBean.class));
        }

        return feedTable;
    }

    @Override
    public void postStatusToFeed(PostStatusRequest request, long currentTime, List<User> followers) {
        FeedBean newPost = new FeedBean();
        for (User follower : followers) {
            newPost.setReceiverAlias(follower.getAlias());
            newPost.setTimestamp(currentTime);
            newPost.setPost(request.getStatus().getPost());
            newPost.setUrls(request.getStatus().getUrls());
            newPost.setMentions(request.getStatus().getMentions());
            newPost.setSenderAlias(request.getStatus().getUser().getAlias());
            newPost.setSenderFirstName(request.getStatus().getUser().getFirstName());
            newPost.setSenderLastName(request.getStatus().getUser().getLastName());
            newPost.setSenderImage(request.getStatus().getUser().getImageUrl());

            getFeedTable().putItem(newPost);
        }
    }

    @Override
    public GetFeedResponse getFeed(GetFeedRequest request) {
        String lastTimestamp;
        if (request.getLastStatus() != null) {
            lastTimestamp = request.getLastStatus().getTimestamp();
        }
        else {
            lastTimestamp = null;
        }

        QueryEnhancedRequest queryRequest = createPagedQueryRequest(request.getUserAlias(), request.getLimit(), lastTimestamp);

        GetFeedResponse response = new GetFeedResponse(new ArrayList<>(), false);

        PageIterable<FeedBean> pages = getFeedTable().query(queryRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedBean> page) -> {
                    response.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(feedBean -> response.getFeedPage().add(feedBean.convertFeedToStatus()));
                });

        return response;
    }

    private QueryEnhancedRequest createPagedQueryRequest(String targetUserAlias, int pageSize, String lastTimestamp) {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if (isNonEmptyString(lastTimestamp)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FEED_PARTITION_KEY, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FEED_SORT_KEY, AttributeValue.builder().n(lastTimestamp).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        return requestBuilder.build();
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
}
