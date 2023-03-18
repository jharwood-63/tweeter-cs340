package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class ServerFacadeIntegrationTest {
    private RegisterRequest registerRequest;
    private RegisterRequest registerRequestError;
    private ServerFacade serverFacade;

    @BeforeEach
    public void setUp() {
        serverFacade = new ServerFacade();
        registerRequest = new RegisterRequest("Thomas", "Jefferson", "@tjeff", "password", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        registerRequestError = new RegisterRequest("", "Jefferson", "@tjeff", "password", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
    }

    @Test
    public void registerTest_Success() throws IOException, TweeterRemoteException {
        RegisterResponse actualResponse = serverFacade.register(registerRequest, "register");

        User expectedUser = new User("Allen", "Anderson", "@allen", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Assertions.assertTrue(actualResponse.isSuccess());
        Assertions.assertEquals(expectedUser, actualResponse.getUser());
        Assertions.assertNotNull(actualResponse.getAuthToken());
    }

    @Test
    public void registerTest_Exception() throws IOException, TweeterRemoteException {
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
}
