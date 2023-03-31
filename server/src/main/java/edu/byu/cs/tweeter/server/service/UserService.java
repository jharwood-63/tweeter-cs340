package edu.byu.cs.tweeter.server.service;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService extends Service {

    private IUserDAO userDAO;

    protected IUserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = getFactory().getUserDAO();
        }

        return userDAO;
    }

    public RegisterResponse register(RegisterRequest request) {
        if(request.getUsername() == null || request.getUsername().equals("")){
            throw new RuntimeException("[Bad Request] Missing a username");
        }
        else if(request.getPassword() == null || request.getPassword().equals("")) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }
        else if(request.getFirstName() == null || request.getFirstName().equals("")) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        }
        else if(request.getLastName() == null || request.getLastName().equals("")) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        }
        else if(request.getImageUrl() == null || request.getImageUrl().equals("")) {
            throw new RuntimeException("[Bad Request] Missing an image");
        }

        User user = getUserDAO().register(request);
        if (user != null) {
            AuthToken authToken = uploadNewAuthToken();
            return new RegisterResponse(user, authToken);
        }

        return new RegisterResponse("Unable to create new user account");
    }

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        User user = getUserDAO().login(request);
        if (user != null) {
            AuthToken authToken = uploadNewAuthToken();
            return new LoginResponse(user, authToken);
        }

        return new LoginResponse("Unable to authenticate user");
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getAlias() == null || request.getAlias().equals("")) {
            throw new RuntimeException("[Bad Request] Missing user alias");
        }

        User retrievedUser = getUserDAO().getUser(request);
        if (retrievedUser != null) {
            return new GetUserResponse(retrievedUser);
        }
        else {
            return new GetUserResponse(false, "Unable to find a user with alias " + request.getAlias());
        }
    }

    public LogoutResponse logout(LogoutRequest request) {
        getAuthTokenDAO().logout(request.getAuthToken());

        return new LogoutResponse();
    }

    private AuthToken uploadNewAuthToken() {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString(), String.valueOf(System.currentTimeMillis()));
        getAuthTokenDAO().login(authToken);
        return authToken;
    }
}
