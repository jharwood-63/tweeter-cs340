package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;

public abstract class Handler<T> {
    private T service;
    private DAOFactory factory;

    protected T getService() {
        if (service == null) {
            service = createService();
        }

        return service;
    }

    protected DAOFactory getFactory() {
        if (factory == null) {
            factory = new DynamoDAOFactory();
        }

        return factory;
    }

    protected abstract T createService();
}
