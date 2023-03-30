package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.server.dao.dynamodb.FollowDAO;
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
}
