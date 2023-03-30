package edu.byu.cs.tweeter.server.dao;

public interface DAOFactory {
    IFollowDAO getFollowDAO();
    IStoryDAO getStatusDAO();
    IUserDAO getUserDAO();
    IFeedDAO getFeedDAO();
    IAuthTokenDAO getAuthTokenDAO();
}
