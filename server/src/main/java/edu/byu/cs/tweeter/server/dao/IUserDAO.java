package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;

public interface IUserDAO {
    void register(RegisterRequest request);
    void verifyCredentials(LoginRequest request);
    User getUser(GetUserRequest request);
    void updateFollowersCount(String followeeAlias, int value);
    void updateFollowingCount(String followerAlias, int value);
    int getFollowingCount(String userAlias);
    int getFollowersCount(String userAlias);
}
