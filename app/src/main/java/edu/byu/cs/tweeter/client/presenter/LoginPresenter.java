package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    public interface View {
        void setErrorView(String message);

        void createToast(String message);

        void displayMessage(String message);

        void startMainActivity(User loggedInUser, String message);
    }

    private View view;

    private UserService userService;

    public LoginPresenter(View view) {
        this.view = view;
        this.userService = new UserService();
    }

    public void login(String alias, String password) {
        try {
            validateLogin(alias, password);
            view.setErrorView(null);

            view.createToast("Logging In ...");

            userService.login(alias, password, new LoginObserver());
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

    private class LoginObserver implements UserService.LoginObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void startMainActivity(User loggedInUser, String message) {
            view.startMainActivity(loggedInUser, message);
        }
    }
}
