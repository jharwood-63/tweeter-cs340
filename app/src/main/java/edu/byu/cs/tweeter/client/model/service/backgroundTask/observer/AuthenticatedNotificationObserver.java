package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface AuthenticatedNotificationObserver<T> extends ServiceObserver {
    void handleSuccess(T displayUpdate);
}
