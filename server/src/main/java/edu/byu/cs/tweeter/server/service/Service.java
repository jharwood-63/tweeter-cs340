package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;

public class Service {
    private IAuthTokenDAO authTokenDAO;

    protected IAuthTokenDAO getAuthTokenDAO(DAOFactory factory) {
        if (authTokenDAO == null) {
            authTokenDAO = factory.getAuthTokenDAO();
        }

        return authTokenDAO;
    }
}
