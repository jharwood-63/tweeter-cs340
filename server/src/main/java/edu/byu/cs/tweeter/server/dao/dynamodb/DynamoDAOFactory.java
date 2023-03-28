package edu.byu.cs.tweeter.server.dao.dynamodb;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.IStatusDAO;
import edu.byu.cs.tweeter.server.dao.IUserDAO;

public class DynamoDAOFactory implements DAOFactory {
    @Override
    public IFollowDAO getFollowDAO() {
        return new FollowDAO();
    }

    @Override
    public IStatusDAO getStatusDAO() {
        return new StatusDAO();
    }

    @Override
    public IUserDAO getUserDAO() {
        return new UserDAO();
    }
}
