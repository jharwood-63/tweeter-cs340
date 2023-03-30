package edu.byu.cs.tweeter.server.dao.dynamodb;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.AuthTokenBean;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DAOUtils {
    private static final String AUTHTOKEN_TABLE_NAME = "AuthToken";
    private static DynamoDbClient client;
    private static DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<AuthTokenBean> authTokenTable;
    private DynamoDbTable<UserBean> userTable;

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

    private DynamoDbTable<AuthTokenBean> getAuthTokenTable() {
        if (authTokenTable == null) {
            authTokenTable = getEnhancedClient().table(AUTHTOKEN_TABLE_NAME, TableSchema.fromBean(AuthTokenBean.class));
        }

        return authTokenTable;
    }

    protected boolean authenticateAuthToken(AuthToken authToken) {
        if (System.currentTimeMillis() - Long.parseLong(authToken.getDatetime()) > 600000) { // 10 minutes
            return false;
        }
        else {
            return compareAuthToken(authToken.getToken());
        }
    }

    private boolean compareAuthToken(String token) {
        Key key = Key.builder().partitionValue(token).build();

        AuthTokenBean authTokenBean = getAuthTokenTable().getItem(key);

        if (authTokenBean != null) {
            resetTimeStamp(authTokenBean);
            return true;
        }
        else {
            return false;
        }
    }

    private void resetTimeStamp(AuthTokenBean authTokenBean) {
        authTokenBean.setDatetime(System.currentTimeMillis());
        getAuthTokenTable().updateItem(authTokenBean);
    }
}
