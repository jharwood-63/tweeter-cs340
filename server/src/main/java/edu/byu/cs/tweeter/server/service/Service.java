package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;

public class Service {
    private DAOFactory factory;
    private IAuthTokenDAO authTokenDAO;

    protected DAOFactory getFactory() {
        if (factory == null) {
            factory = new DynamoDAOFactory();
        }

        return factory;
    }

    protected IAuthTokenDAO getAuthTokenDAO() {
        if (authTokenDAO == null) {
            authTokenDAO = getFactory().getAuthTokenDAO();
        }

        return authTokenDAO;
    }
}
