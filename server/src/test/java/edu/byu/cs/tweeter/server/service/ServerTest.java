package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
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
        GetFollowingRequest request = new GetFollowingRequest(new AuthToken(), "@FredFlinstone", 5, null);
        FollowDAO followDAO = new FollowDAO();
        followDAO.getFollowing(request);
    }

    @Test
    public void testPostStatus() {
        AuthToken authToken = new AuthToken("This is a token", Long.toString(System.currentTimeMillis()));
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        StatusService statusService = new StatusService();

        User user = new User("Fred", "Flinstone", "imageUrl");
        PostStatusRequest request = new PostStatusRequest(authToken, new Status("test post", user, Long.toString(System.currentTimeMillis()), new ArrayList<String>() {{
            add("https://youtube.com");
        }}, new ArrayList<String>() {{
            add("@Dude1");
        }}));

        authTokenDAO.login(authToken);
        statusService.postStatus(request);
    }
}
