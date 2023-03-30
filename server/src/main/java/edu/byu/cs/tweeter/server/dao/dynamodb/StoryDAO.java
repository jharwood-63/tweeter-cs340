package edu.byu.cs.tweeter.server.dao.dynamodb;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.IStoryDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.FeedBean;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.StoryBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class StoryDAO extends DAOUtils implements IStoryDAO {
    private static final String STORY_TABLE_NAME = "story";
    private static final String STORY_PARTITION_KEY = "senderAlias";
    private static final String STORY_SORT_KEY = "timestamp";

    private DynamoDbTable<FeedBean> feedTable;
    private DynamoDbTable<StoryBean> storyTable;

    private DynamoDbTable<StoryBean> getStoryTable() {
        if (storyTable == null) {
            storyTable = getEnhancedClient().table(STORY_TABLE_NAME, TableSchema.fromBean(StoryBean.class));
        }

        return storyTable;
    }

    @Override
    public void postStatusToStory(PostStatusRequest request, long currentTime) {
        StoryBean newPost = new StoryBean();
        newPost.setSenderAlias(request.getStatus().getUser().getAlias());
        newPost.setTimestamp(currentTime);
        newPost.setPost(request.getStatus().getPost());
        newPost.setUrls(request.getStatus().getUrls());
        newPost.setMentions(request.getStatus().getMentions());

        getStoryTable().putItem(newPost);
    }

    @Override
    public GetStoryResponse getStory(GetStoryRequest request) {
        return null;
    }
}
