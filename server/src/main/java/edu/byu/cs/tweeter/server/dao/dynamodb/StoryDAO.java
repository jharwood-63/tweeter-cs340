package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.IStoryDAO;
import edu.byu.cs.tweeter.server.dto.StoryDTO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class StoryDAO extends DAOUtils implements IStoryDAO {
    private static final String STORY_TABLE_NAME = "story";
    private static final String STORY_PARTITION_KEY = "senderAlias";
    private static final String STORY_SORT_KEY = "timestamp";

    private DynamoDbTable<StoryDTO> storyTable;

    private DynamoDbTable<StoryDTO> getStoryTable() {
        if (storyTable == null) {
            storyTable = getEnhancedClient().table(STORY_TABLE_NAME, TableSchema.fromBean(StoryDTO.class));
        }

        return storyTable;
    }

    @Override
    public void postStatusToStory(PostStatusRequest request, long currentTime) {
        StoryDTO newPost = new StoryDTO();
        newPost.setSenderAlias(request.getStatus().getUser().getAlias());
        newPost.setTimestamp(currentTime);
        newPost.setPost(request.getStatus().getPost());
        newPost.setUrls(request.getStatus().getUrls());
        newPost.setMentions(request.getStatus().getMentions());
        newPost.setSenderFirstName(request.getStatus().getUser().getFirstName());
        newPost.setSenderLastName(request.getStatus().getUser().getLastName());
        newPost.setSenderImage(request.getStatus().getUser().getImageUrl());

        getStoryTable().putItem(newPost);
    }

    @Override
    public GetStoryResponse getStory(GetStoryRequest request) {
        String lastTimestamp;
        if (request.getLastStatus() != null) {
            lastTimestamp = request.getLastStatus().getTimestamp();
        }
        else {
            lastTimestamp = null;
        }

        QueryEnhancedRequest queryRequest = createPagedQueryRequest(request.getUserAlias(), request.getLimit(), lastTimestamp);

        GetStoryResponse response = new GetStoryResponse(new ArrayList<>(), false);

        PageIterable<StoryDTO> pages = getStoryTable().query(queryRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<StoryDTO> page) -> {
                    response.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(storyDTO -> response.getStoryPage().add(storyDTO.convertStoryToStatus()));
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
            startKey.put(STORY_PARTITION_KEY, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(STORY_SORT_KEY, AttributeValue.builder().n(lastTimestamp).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        return requestBuilder.build();
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
}
