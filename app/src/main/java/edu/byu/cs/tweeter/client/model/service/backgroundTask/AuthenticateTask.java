package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    private User authenticatedUser;

    private AuthToken authToken;

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected final String username;

    /**
     * The user's password.
     */
    protected final String password;

    protected AuthenticateTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }


    @Override
    protected final void runTask() {
        try {
            Pair<User, AuthToken> loginResult = runAuthenticationTask(username, password);

            if (loginResult != null) {
                authenticatedUser = loginResult.getFirst();
                authToken = loginResult.getSecond();

                // Call sendSuccessMessage if successful
                sendSuccessMessage();
            }
            else {
                sendFailedMessage("Unable to authenticate");
            }
        }
        catch (Exception e) {
            sendExceptionMessage(e);
        }
    }

    protected abstract Pair<User, AuthToken> runAuthenticationTask(String username, String password) throws Exception;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
