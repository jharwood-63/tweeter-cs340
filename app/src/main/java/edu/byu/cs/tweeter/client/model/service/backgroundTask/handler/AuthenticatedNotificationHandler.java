package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;

public abstract class AuthenticatedNotificationHandler<T> extends BackgroundTaskHandler<AuthenticatedNotificationObserver> {

    public AuthenticatedNotificationHandler(AuthenticatedNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, AuthenticatedNotificationObserver observer) {
        T item = getDisplayUpdate(data);
        observer.handleSuccess(item);
    }

    protected abstract T getDisplayUpdate(Bundle data);
}
