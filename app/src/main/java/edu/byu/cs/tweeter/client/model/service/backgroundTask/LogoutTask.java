package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
    }

    @Override
    protected void runTask() {
        try {
            LogoutRequest request = new LogoutRequest(authToken.getToken());
            LogoutResponse response = getServerFacade().logout(request, "logout");

            if (response.isSuccess()) {
                sendSuccessMessage();
            }
            else {
                sendFailedMessage(response.getMessage());
            }
        }
        catch (Exception e) {
            sendExceptionMessage(e);
        }
    }
}
