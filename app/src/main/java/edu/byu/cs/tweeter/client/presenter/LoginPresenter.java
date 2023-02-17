package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticateNotificationObserver;

public class LoginPresenter extends AuthenticatePresenter implements AuthenticateNotificationObserver {
    public LoginPresenter(AuthenticateView view) {
        super(view);
    }

    public void login(String alias, String password) {
        try {
            validateLogin(alias, password);
            setupAuthentication("Logging In...");

            getUserService().login(alias, password, this);
        } catch (Exception e) {
            setErrorView(e.getMessage());
        }
    }

    public void validateLogin(String alias, String password) {
        validateAlias(alias);
        validatePassword(password);
    }

    @Override
    protected String getMessagePrefix() {
        return "Failed to login: ";
    }

    @Override
    protected String getExceptionPrefix() {
        return "Failed to login because of exception: ";
    }
}
