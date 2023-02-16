package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;

public class IsFollowerHandler extends AuthenticatedNotificationHandler {

    public IsFollowerHandler(AuthenticatedNotificationObserver<Boolean> observer) {
        super(observer);
    }

    @Override
    protected Boolean getDisplayUpdate(Bundle data) {
        return data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
    }
}
