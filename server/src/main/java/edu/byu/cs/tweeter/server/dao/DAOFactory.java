package edu.byu.cs.tweeter.server.dao;

public interface DAOFactory {
    IFollowDAO getFollowDAO();
    IStatusDAO getStatusDAO();
    IUserDAO getUserDAO();
}
