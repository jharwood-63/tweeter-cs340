package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.server.dto.UserDTO;

public interface IUserDAO {
    User register(RegisterRequest request);
    User login(LoginRequest request);
    User getUser(GetUserRequest request);
    void decrementFollowersCount(String followeeAlias);
    void incrementFollowersCount(String followeeAlias);
    void decrementFollowingCount(String followerAlias);
    void incrementFollowingCount(String followerAlias);
    int getFollowingCount(String userAlias);
    int getFollowersCount(String userAlias);
    void addUserBatch(List<UserDTO> users);
    void deleteAllUsers(List<UserDTO> users);
}
