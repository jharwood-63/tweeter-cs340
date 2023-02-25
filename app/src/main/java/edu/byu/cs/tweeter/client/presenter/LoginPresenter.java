package edu.byu.cs.tweeter.client.presenter;

public class LoginPresenter extends AuthenticatePresenter {
    public LoginPresenter(AuthenticateView view) {
        super(view);
    }

    public void login(String alias, String password) {
        try {
            validateLogin(alias, password);
            setupAuthentication("Logging In...");

            getUserService().login(alias, password, new AuthenticateObserver());
        } catch (Exception e) {
            setErrorView(e.getMessage());
        }
    }

    public void validateLogin(String alias, String password) {
        validateAlias(alias);
        validatePassword(password);
    }
}
