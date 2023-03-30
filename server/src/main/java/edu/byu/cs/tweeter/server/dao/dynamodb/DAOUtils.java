package edu.byu.cs.tweeter.server.dao.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DAOUtils {
    private static DynamoDbClient client;
    private static DynamoDbEnhancedClient enhancedClient;

    private DynamoDbClient getClient() {
        if (client == null) {
            client = DynamoDbClient.builder().region(Region.US_WEST_2).build();
        }

        return client;
    }

    protected DynamoDbEnhancedClient getEnhancedClient() {
        if (enhancedClient == null) {
            enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(getClient()).build();
        }

        return enhancedClient;
    }
}
