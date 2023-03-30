package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;

public abstract class Handler<T> {
    private T service;

    protected T getService() {
        if (service == null) {
            service = createService();
        }

        return service;
    }

    protected abstract T createService();
}
