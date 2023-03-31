package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.StoryDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.UserDAO;

public class ServerTest {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    @Test
    public void testUploadImage() {
        UserDAO userDAO = new UserDAO();
        userDAO.uploadImageToS3(MALE_IMAGE_URL, "@test");
    }

    @Test
    public void testGetFollowing() {
        AuthToken authToken = new AuthToken("This is a token", Long.toString(System.currentTimeMillis()));
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        authTokenDAO.login(authToken);

        User james = new User("James", "Talmage", "@jt", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40jt");
        User brigham = new User("Brigham", "Young", "@BYU", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40BYU");
        GetFollowingRequest request = new GetFollowingRequest(authToken, james.getAlias(), 5, null);
        FollowDAO followDAO = new FollowDAO();
        GetFollowingResponse response = followDAO.getFollowing(request);

        for(User followee : response.getFollowees()) {
            System.out.println(followee.toString());
        }
    }

    @Test
    public void testGetFollowers() {
        AuthToken authToken = new AuthToken("This is a token", Long.toString(System.currentTimeMillis()));
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        authTokenDAO.login(authToken);

        User james = new User("James", "Talmage", "@jt", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40jt");
        User brigham = new User("Brigham", "Young", "@BYU", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40BYU");
        GetFollowersRequest request = new GetFollowersRequest(authToken, brigham.getAlias(), 5, null);
        FollowDAO followDAO = new FollowDAO();
        GetFollowersResponse response = followDAO.getFollowers(request);

        for(User follower : response.getFollowers()) {
            System.out.println(follower.toString());
        }
    }

    @Test
    public void testPostStatus() {
        AuthToken authToken = new AuthToken("This is a token", Long.toString(System.currentTimeMillis()));
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        StatusService statusService = new StatusService();

        User user = new User("James", "Talmage", "@jt", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40jt");
        PostStatusRequest request = new PostStatusRequest(authToken, new Status("first post", user, Long.toString(System.currentTimeMillis()), new ArrayList<String>() {{
            add("https://youtube.com");
        }}, new ArrayList<String>() {{
            add("@Dude1");
        }}));

        authTokenDAO.login(authToken);
        statusService.postStatus(request);
    }

    @Test
    public void testGetFeed() {
        AuthToken authToken = new AuthToken("This is a token", Long.toString(System.currentTimeMillis()));

        User user = new User("Fred", "Flinstone", "imageUrl");
        Status status = new Status("test post", user, Long.toString(System.currentTimeMillis()), new ArrayList<String>() {{
            add("https://youtube.com");
        }}, new ArrayList<String>() {{
            add("@Dude1");
        }});
        GetFeedRequest request = new GetFeedRequest(authToken, "@Jackson", 5, null);
        StatusService statusService = new StatusService();
        statusService.getFeed(request);
    }

    @Test
    public void testLogin() {
        User expectedUser = new User("Jack", "Hale", "@jman", "https://tweeter4340.s3.us-west-2.amazonaws.com/%40jman");
        LoginRequest request = new LoginRequest("@jman", "password");
        UserService userService = new UserService();

        LoginResponse response = userService.login(request);

        Assertions.assertEquals(expectedUser, response.getUser());
    }

    @Test
    public void testUnfollow() {
        User james = new User("James", "Talmage", "@jt", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40jt");
        User brigham = new User("Brigham", "Young", "@BYU", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40BYU");

        AuthToken authToken = new AuthToken("This is a token", Long.toString(System.currentTimeMillis()));
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        authTokenDAO.login(authToken);

        UnfollowRequest unfollowRequest = new UnfollowRequest(authToken, james, brigham);
        FollowService followService = new FollowService();
        followService.unfollow(unfollowRequest);
    }

    @Test
    public void testFollow() {
        User james = new User("James", "Talmage", "@jt", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40jt");
        User brigham = new User("Brigham", "Young", "@BYU", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40BYU");

        AuthToken authToken = new AuthToken("This is a token", Long.toString(System.currentTimeMillis()));
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        authTokenDAO.login(authToken);

        FollowRequest followRequest = new FollowRequest(authToken, james, brigham);
        FollowService followService = new FollowService();
        followService.follow(followRequest);
    }

    @Test
    public void testGetFollowingCount() {
        User james = new User("James", "Talmage", "@jt", "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40jt");
        AuthToken authToken = new AuthToken("This is a token", Long.toString(System.currentTimeMillis()));
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        authTokenDAO.login(authToken);

        GetCountRequest request = new GetCountRequest(authToken, james.getAlias());
        FollowService followService = new FollowService();

        GetCountResponse followingCountResponse = followService.getFollowingCount(request);
        System.out.println("Following count: " + followingCountResponse.getCount());

        GetCountResponse followersCountResponse = followService.getFollowersCount(request);
        System.out.println("Followers count: " + followersCountResponse.getCount());
    }
}
