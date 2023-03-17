package edu.byu.cs.tweeter.client.model.net;

import android.os.Handler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowListTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticateNotificationHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class ApiTest {
    private final AuthToken authToken = new AuthToken("12345", "3/15/23");
    private final User targetUser = new User("Jackson", "Harwood", "@alias", "imageurl");

    @Test
    public void unfollowTest() {
        UnfollowTask unfollowTask = new UnfollowTask(authToken, targetUser, null);
        unfollowTask.run();
    }

    @Test
    public void followTest() {
        FollowTask followTask = new FollowTask(authToken, targetUser, null);
        followTask.run();
    }

    @Test
    public void getFollowingTest() {
        User lastItem = new User("Alexis", "Harwood", "@another", "anotherurl");
        GetFollowListTask followListTask = new GetFollowListTask(authToken, targetUser, 5, lastItem, null);
        followListTask.run();
    }

    @Test
    public void testLogin() {
        Handler handlerMock = Mockito.mock(AuthenticateNotificationHandler.class);
        LoginTask loginTask = new LoginTask("@user", "pass", null);
        loginTask.run();
    }

    @Test
    public void testGetFollowersCount() {
        GetFollowersCountTask getFollowersCountTask = new GetFollowersCountTask(authToken, targetUser, null);
        getFollowersCountTask.run();
    }

    @Test
    public void testGetFollowingCount() {
        GetFollowingCountTask getFollowingCountTask = new GetFollowingCountTask(authToken, targetUser, null);
        getFollowingCountTask.run();
    }
}
