package edu.byu.cs.tweeter.client.model.net;

import android.os.Handler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowListTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticateNotificationHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class ApiTest {
    private final AuthToken authToken = new AuthToken("12345", "3/15/23");

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private static final User targetUser = new User("Allen", "Anderson", "@allen", MALE_IMAGE_URL);
    private static final User lastUser = new User("Amy", "Ames", "@amy", FEMALE_IMAGE_URL);

    @Test
    public void unfollowTest() {
        UnfollowTask unfollowTask = new UnfollowTask(authToken, targetUser, lastUser, null);
        unfollowTask.run();
    }

    @Test
    public void followTest() {
        FollowTask followTask = new FollowTask(authToken, targetUser, lastUser, null);
        followTask.run();
    }

    @Test
    public void getFollowingTest() {
        GetFollowListTask followListTask = new GetFollowingTask(authToken, targetUser, 5, lastUser, null);
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
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, lastUser, targetUser, null);
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

    @Test
    public void testGetFeed() {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, targetUser, 5, null, null);
        getFeedTask.run();
    }

    @Test
    public void testLogout() {
        LogoutTask logoutTask = new LogoutTask(authToken, null);
        logoutTask.run();
    }

    @Test
    public void testRegister() {
        RegisterTask registerTask = new RegisterTask("", "last", "@alias", "pass", "image", null);
        registerTask.run();
    }

    @Test
    public void testGetFollowers() {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, targetUser, 5, lastUser, null);
        getFollowersTask.run();
    }

    @Test
    public void testGetStory() {
        GetStoryTask getStoryTask = new GetStoryTask(authToken, targetUser, 5, null, null);
        getStoryTask.run();
    }
}
