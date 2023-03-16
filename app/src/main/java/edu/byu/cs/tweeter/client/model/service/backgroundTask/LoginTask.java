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

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);

        try {
            //FIXME: NVM I think it is now
            LoginResponse loginResponse = getServerFacade().login(loginRequest, "login");
            return new Pair<>(loginResponse.getUser(), loginResponse.getAuthToken());
        }
        catch (TweeterRemoteException | IOException e) {
            System.out.println(e.getMessage());
            throw new Exception("Exception caught while logging in");
        }
    }
}
