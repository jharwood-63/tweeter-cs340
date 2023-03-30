package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.FeedBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

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

            getFeedTable().putItem(newPost);
        }
    }

    @Override
    public GetFeedResponse getFeed(GetFeedRequest request) {
        return null;
    }
}
