package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.IFeedDAO;
import edu.byu.cs.tweeter.server.dto.FeedDTO;
import edu.byu.cs.tweeter.server.dto.FollowDTO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class FeedDAO extends DAOUtils implements IFeedDAO {
    private static final String FEED_TABLE_NAME = "feed";
    private static final String FEED_PARTITION_KEY = "receiverAlias";
    private static final String FEED_SORT_KEY = "timestamp";

    private DynamoDbTable<FeedDTO> feedTable;

    private DynamoDbTable<FeedDTO> getFeedTable() {
        if (feedTable == null) {
            feedTable = getEnhancedClient().table(FEED_TABLE_NAME, TableSchema.fromBean(FeedDTO.class));
        }

        return feedTable;
    }

    @Override
    public void postStatusToFeed(PostStatusRequest request, long currentTime, List<User> followers) {
        FeedDTO newPost = new FeedDTO();
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

        PageIterable<FeedDTO> pages = getFeedTable().query(queryRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedDTO> page) -> {
                    response.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(feedDTO -> response.getFeedPage().add(feedDTO.convertFeedToStatus()));
                });

        return response;
    }

    @Override
    public void clearFeed(List<String> receivers, long timestamp) {
        List<String> batch = new ArrayList<>();
        for (String receiver : receivers) {
            batch.add(receiver);

            if (batch.size() == 25) {
                deleteBatchOfFeedDTOs(batch, timestamp);
                batch = new ArrayList<>();
            }
        }

        if (batch.size() > 0) {
            deleteBatchOfFeedDTOs(batch, timestamp);
        }
    }

    private void deleteBatchOfFeedDTOs(List<String> batch, long timestamp) {
        if(batch.size() > 25) {
            throw new RuntimeException("Too many followers to delete");
        }

        WriteBatch.Builder<FeedDTO> writeBuilder = WriteBatch.builder(FeedDTO.class).mappedTableResource(getFeedTable());
        for (String receiver : batch) {
            Key key = Key.builder()
                    .partitionValue(receiver)
                    .sortValue(timestamp)
                    .build();
            writeBuilder.addDeleteItem(builder -> builder.key(key));
        }

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = getEnhancedClient().batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedDeleteItemsForTable(getFeedTable()).size() > 0) {
                System.out.println("There were items that were unprocessed");
//                deleteBatchOfFeedDTOs(result.unprocessedPutItemsForTable(getFeedTable()), timestamp);
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
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
