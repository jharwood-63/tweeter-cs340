package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import static java.lang.System.exit;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {
    private LoginResponse response;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() throws Exception {
        LoginRequest request = new LoginRequest(username, password);
        response = getServerFacade().login(request, "login");

        if (response.isSuccess()) {
            return new Pair<>(response.getUser(), response.getAuthToken());
        }
        else {
            return null;
        }
    }

    @Override
    protected String getFailedMessage() {
        return response.getMessage();
    }
}
