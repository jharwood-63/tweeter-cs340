package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class ServerFacadeIntegrationTest {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User expectedUser = new User("Allen", "Anderson", "@allen", MALE_IMAGE_URL);
    private ServerFacade serverFacade;

    @BeforeEach
    public void setUp() {
        serverFacade = new ServerFacade();
    }

    @Test
    public void registerTest_Success() throws IOException, TweeterRemoteException {
        RegisterRequest registerRequest = new RegisterRequest("Thomas", "Jefferson", "@tjeff", "password", MALE_IMAGE_URL);

        RegisterResponse actualResponse = serverFacade.register(registerRequest, "register");

        Assertions.assertTrue(actualResponse.isSuccess());
        Assertions.assertEquals(expectedUser, actualResponse.getUser());
        Assertions.assertNotNull(actualResponse.getAuthToken());
    }

    @Test
    public void registerTest_Exception() throws IOException, TweeterRemoteException {
        RegisterRequest registerRequestError = new RegisterRequest("", "Jefferson", "@tjeff", "password", MALE_IMAGE_URL);

        try {
            RegisterResponse response = serverFacade.register(registerRequestError, "register");
            Assertions.assertNull(response);
        }
        catch (TweeterRequestException e) {
            Assertions.assertEquals("[Bad Request] Missing a first name", e.getMessage());
            Assertions.assertEquals("java.lang.RuntimeException", e.getRemoteExceptionType());
            Assertions.assertNotEquals(0, e.getStackTrace().length);
        }
    }

    @Test
    public void getFollowersTest_Success() throws IOException, TweeterRemoteException {
        GetFollowersRequest getFollowersRequest = new GetFollowersRequest(new AuthToken(), "@allen", 5, "@amy");

        GetFollowersResponse actualResponse = serverFacade.getFollowers(getFollowersRequest, "getfollowers");

        User expectedFirstFollower = new User("Bob", "Bobson", "@bob", MALE_IMAGE_URL);
        User expectedLastFollower = new User("Dan", "Donaldson", "@dan", MALE_IMAGE_URL);

        Assertions.assertTrue(actualResponse.isSuccess());
        Assertions.assertTrue(actualResponse.getHasMorePages());
        Assertions.assertEquals(5, actualResponse.getFollowers().size());
        Assertions.assertEquals(expectedFirstFollower, actualResponse.getFollowers().get(0));
        Assertions.assertEquals(expectedLastFollower, actualResponse.getFollowers().get(actualResponse.getFollowers().size() - 1));
    }

    @Test
    public void getFollowersTest_Exception() throws IOException, TweeterRemoteException {
        GetFollowersRequest getFollowersRequestError = new GetFollowersRequest(new AuthToken(), "", 5, "@amy");

        try {
            GetFollowersResponse response = serverFacade.getFollowers(getFollowersRequestError, "getfollowers");
            Assertions.assertNull(response);
        }
        catch (TweeterRequestException e) {
            Assertions.assertEquals("[Bad Request] Request needs to have a followee alias", e.getMessage());
            Assertions.assertEquals("java.lang.RuntimeException", e.getRemoteExceptionType());
            Assertions.assertNotEquals(0, e.getStackTrace().length);
        }
    }

    @Test
    public void getFollowersCountTest_Success() throws IOException, TweeterRemoteException {
        GetCountRequest getCountRequest = new GetCountRequest(new AuthToken(), "@allen");

        GetCountResponse actualResponse = serverFacade.getCount(getCountRequest, "getfollowerscount");

        Assertions.assertTrue(actualResponse.isSuccess());
        Assertions.assertEquals(21, actualResponse.getCount());
    }

    @Test
    public void getFollowersCountTest_Exception() throws IOException, TweeterRemoteException {
        GetCountRequest getCountRequestError = new GetCountRequest(new AuthToken(), "");

        try {
            GetCountResponse response = serverFacade.getCount(getCountRequestError, "getfollowerscount");
            Assertions.assertNull(response);
        }
        catch (TweeterRequestException e) {
            Assertions.assertEquals("[Bad Request] Request must have a user", e.getMessage());
            Assertions.assertEquals("java.lang.RuntimeException", e.getRemoteExceptionType());
            Assertions.assertNotEquals(0, e.getStackTrace().length);
        }
    }
}
