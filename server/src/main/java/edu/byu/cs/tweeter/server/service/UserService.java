package edu.byu.cs.tweeter.server.service;

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

public class UserService {

    private final IUserDAO userDAO;

    public UserService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    protected IUserDAO getUserDAO() {
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

        getUserDAO().register(request);

        //FIXME: REMOVE THIS
        User registeredUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();

        return new RegisterResponse(registeredUser, authToken);
    }

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (!getUserDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getAlias() == null || request.getAlias().equals("")) {
            throw new RuntimeException("[Bad Request] Missing user alias");
        }

        User user = getFakeData().findUserByAlias(request.getAlias());
        if (user != null) {
            return new GetUserResponse(user);
        }
        else {
            return new GetUserResponse(false, "Unable to find a user with alias " + request.getAlias());
        }
    }

    public LogoutResponse logout(LogoutRequest request) {
        // No need to authenticate the user if they are logging out right?
//        if (!getUserDAO().authenticateRequest(request.getAuthToken())) {
//            throw new RuntimeException("[Bad Request] Unauthenticated User");
//        }

        return new LogoutResponse();
    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
