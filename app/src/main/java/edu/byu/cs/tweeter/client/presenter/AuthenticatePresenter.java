package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatePresenter extends Presenter implements AuthenticateNotificationObserver {

    public interface AuthenticateView extends PresenterView {
        void setErrorView(String message);

        void createToast(String message);

        void startMainActivity(User authenticatedUser, String message);
    }
    private UserService userService;

    public AuthenticatePresenter(AuthenticateView view) {
        super(view);
        this.userService = new UserService();
    }

    public UserService getUserService() {
        return userService;
    }

    public AuthenticateView getAuthenticateView() {
        return ((AuthenticateView) getView());
    }

    protected void setErrorView(String message) {
        getAuthenticateView().setErrorView(message);
    }

    protected void setupAuthentication(String message) {
        setErrorView(null);
        getAuthenticateView().createToast(message);
    }

    protected void validateAlias(String alias) {
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
    }

    protected void validatePassword(String password) {
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    @Override
    public void handleSuccess(User user, String message) {
        getAuthenticateView().startMainActivity(user, message);
    }
}
