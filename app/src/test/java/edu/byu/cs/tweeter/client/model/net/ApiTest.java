package edu.byu.cs.tweeter.client.model.net;

import android.os.Handler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowListTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticateNotificationHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class ApiTest {
    private final AuthToken authToken = new AuthToken("12345", "3/15/23");
    private final User targetUser = new User("Jackson", "Harwood", "@alias", "imageurl");
    private final User lastItem = new User("Alexis", "Harwood", "@another", "anotherurl");

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

    @Test
    public void testIsFollower() {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, lastItem, targetUser, null);
        isFollowerTask.run();
    }

    @Test
    public void testPostStatus() {
        List<String> urls = new ArrayList<>(Arrays.asList("first url", "second url"));
        List<String> mentions = new ArrayList<>(Arrays.asList("first mention", "second mention"));
        Status status = new Status("test post", targetUser, "timestamp", urls, mentions);
        PostStatusTask postStatusTask = new PostStatusTask(authToken, status, null);

        postStatusTask.run();
    }

    @Test
    public void testGetUser() {
        GetUserTask getUserTask = new GetUserTask(authToken, "@allen", null);
        getUserTask.run();
    }
}
