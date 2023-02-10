package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {
    public interface Observer {
        void displayMessage(String message);

        void showUser(User user);
    }

    public interface LogoutObserver {

        void displayMessage(String message);

        void logoutUser();
    }

    public interface LoginObserver {
        void displayMessage(String message);

        void startMainActivity(User loggedInUser, String message);
    }

    public interface RegisterObserver {
        void displayMessage(String message);

        void startMainActivity(User registeredUser, String message);
    }

    public void getUser(String userAlias, Observer observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
        observer.displayMessage("Getting user's profile...");
    }

    public void logout(LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }

    public void login(String alias, String password, LoginObserver observer) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password,
                         String imageBytesBase64, RegisterObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new RegisterHandler(observer));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }
}
