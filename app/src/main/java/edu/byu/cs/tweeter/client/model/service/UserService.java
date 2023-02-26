package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticateNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends Service {
    public void getUser(String userAlias, AuthenticatedNotificationObserver<User> observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(observer));
        runTask(getUserTask);
    }

    public void logout(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer));
        runTask(logoutTask);
    }

    public void login(String alias, String password, AuthenticateNotificationObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticateNotificationHandler(observer));
        runTask(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password,
                         String imageBytesBase64, AuthenticateNotificationObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new AuthenticateNotificationHandler(observer));
        runTask(registerTask);
    }
}
