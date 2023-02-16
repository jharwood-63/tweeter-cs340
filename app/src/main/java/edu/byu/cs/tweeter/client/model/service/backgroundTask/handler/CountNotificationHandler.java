package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;

public class CountNotificationHandler extends AuthenticatedNotificationHandler {

    public CountNotificationHandler(AuthenticatedNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected Object getDisplayUpdate(Bundle data) {
        return data.getInt(GetCountTask.COUNT_KEY);
    }
}
