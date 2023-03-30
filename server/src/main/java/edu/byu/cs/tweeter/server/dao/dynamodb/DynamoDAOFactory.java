package edu.byu.cs.tweeter.server.dao.dynamodb;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.IStoryDAO;
import edu.byu.cs.tweeter.server.dao.IUserDAO;

public class DynamoDAOFactory implements DAOFactory {
    @Override
    public IFollowDAO getFollowDAO() {
        return new FollowDAO();
    }

    @Override
    public IStoryDAO getStatusDAO() {
        return new StoryDAO();
    }

    @Override
    public IUserDAO getUserDAO() {
        return new UserDAO();
    }

    @Override
    public IFeedDAO getFeedDAO() {
        return new FeedDAO();
    }

    @Override
    public IAuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
