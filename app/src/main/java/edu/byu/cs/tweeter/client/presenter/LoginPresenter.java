package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthenticatePresenter implements AuthenticateNotificationObserver {
    private AuthenticateView view;
    
    public LoginPresenter(AuthenticateView view) {
        this.view = view;
    }

    public void login(String alias, String password) {
        try {
            validateLogin(alias, password);
            view.setErrorView(null);

            view.createToast("Logging In ...");

            getUserService().login(alias, password, this);
        } catch (Exception e) {
            view.setErrorView(e.getMessage());
        }
    }

    public void validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    @Override
    public void handleFailure(String message) {
        view.displayMessage("Failed to login: " + message);
    }

    @Override
    public void handleException(Exception ex) {
        view.displayMessage("Failed to login because of exception: " + ex.getMessage());
    }

    @Override
    public void handleSuccess(User loggedInUser, String message) {
        view.startMainActivity(loggedInUser, message);
    }
}
