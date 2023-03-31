package edu.byu.cs.tweeter.server.dao.dynamodb;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.AuthTokenBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class AuthTokenDAO extends DAOUtils implements IAuthTokenDAO {
    private static final String AUTHTOKEN_TABLE_NAME = "AuthToken";
    private DynamoDbTable<AuthTokenBean> authTokenTable;
    private DynamoDbTable<AuthTokenBean> getAuthTokenTable() {
        if (authTokenTable == null) {
            authTokenTable = getEnhancedClient().table(AUTHTOKEN_TABLE_NAME, TableSchema.fromBean(AuthTokenBean.class));
        }

        return authTokenTable;
    }

    @Override
    public boolean authenticateRequest(AuthToken authToken) {
        if (System.currentTimeMillis() - Long.parseLong(authToken.getDatetime()) > 600000) { // 10 minutes
            return false;
        }
        else {
            return compareAuthToken(authToken.getToken());
        }
    }

    @Override
    public void login(AuthToken authToken) {
        AuthTokenBean authTokenBean = new AuthTokenBean();
        authTokenBean.setDatetime(authToken.getDatetime());
        authTokenBean.setToken(authToken.getToken());

        getAuthTokenTable().putItem(authTokenBean);
    }

    @Override
    public void logout(AuthToken authToken) {
        Key key = Key.builder().partitionValue(authToken.getToken()).build();
        getAuthTokenTable().deleteItem(key);
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
        authTokenBean.setDatetime(Long.toString(System.currentTimeMillis()));
        getAuthTokenTable().updateItem(authTokenBean);
    }
}
