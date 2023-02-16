package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticatePresenter extends Presenter {
    public interface AuthenticateView extends PresenterView {
        void setErrorView(String message);

        void createToast(String message);

        void startMainActivity(User authenticatedUser, String message);
    }

    private UserService userService;

    public AuthenticatePresenter() {
        this.userService = new UserService();
    }

    public UserService getUserService() {
        return userService;
    }
}
