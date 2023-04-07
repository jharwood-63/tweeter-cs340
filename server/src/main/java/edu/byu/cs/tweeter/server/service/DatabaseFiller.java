package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.server.dto.FollowDTO;
import edu.byu.cs.tweeter.server.dto.UserDTO;

public class DatabaseFiller {
    private static final int NUM_USERS = 10;
    private static final String FOLLOWEE_ALIAS = "@jt";
    private static final String FOLLOWEE_NAME = "James Talmage";
    private static final String FOLLOWEE_IMAGE = "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40jt";

    private final DAOFactory factory;

    public DatabaseFiller(DAOFactory factory) {
        this.factory = factory;
    }

    public void fillDatabase() {
        IUserDAO userDAO = factory.getUserDAO();
        IFollowDAO followDAO = factory.getFollowDAO();

        List<FollowDTO> followers = new ArrayList<>();
        List<UserDTO> users = new ArrayList<>();

        for (int i = 1; i <= NUM_USERS; i++) {

            String name = "Guy " + i;
            String alias = "@guy" + i;

            UserDTO user = new UserDTO();
            user.setAlias(alias);
            user.setFirstName(name);
            users.add(user);

            FollowDTO follow = new FollowDTO();
            follow.setFollow_handle(alias);
            follow.setFollow_name(name);
            follow.setFollowee_handle(FOLLOWEE_ALIAS);
            follow.setFollowee_name(FOLLOWEE_NAME);
            follow.setFollowee_image(FOLLOWEE_IMAGE);

            followers.add(follow);
        }

        if (users.size() > 0) {
            userDAO.addUserBatch(users);
        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers);
        }
    }
}
