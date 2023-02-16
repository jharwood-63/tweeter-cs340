package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends AuthenticatedNotificationHandler {
    public GetUserHandler(AuthenticatedNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected Object getDisplayUpdate(Bundle data) {
        return data.getSerializable(GetUserTask.USER_KEY);
    }
}
