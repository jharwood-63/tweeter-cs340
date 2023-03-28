package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;

public abstract class Handler<T, U> {
    private DAOFactory factory;
    private T service;

    private DAOFactory getFactory() {
        if (factory == null) {
            factory = new DynamoDAOFactory();
        }

        return factory;
    }

    protected T getService() {
        if (service == null) {
            service = createService(createDAO(getFactory()));
        }

        return service;
    }

    protected abstract U createDAO(DAOFactory factory);

    protected abstract T createService(U dao);
}
