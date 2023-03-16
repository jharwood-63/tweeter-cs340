package edu.byu.cs.tweeter.client.model.net;

import android.os.Handler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticateNotificationHandler;

public class LoginTaskTest {
    @Test
    public void testLogin() {
        Handler handlerMock = Mockito.mock(AuthenticateNotificationHandler.class);
        LoginTask loginTask = new LoginTask("@user", "pass", null);
        loginTask.run();
    }
}
