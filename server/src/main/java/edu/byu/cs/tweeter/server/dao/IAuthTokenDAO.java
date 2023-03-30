package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface IAuthTokenDAO {
    boolean authenticateRequest(AuthToken authToken);
    void login(AuthToken authToken);
    void logout(AuthToken authToken);
}
