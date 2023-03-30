package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;

public interface IUserDAO {
    void register(RegisterRequest request);
    void verifyCredentials(LoginRequest request);
    void getUser(GetUserRequest request);
}
