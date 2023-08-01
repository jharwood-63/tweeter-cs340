package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.server.dto.FollowDTO;
import edu.byu.cs.tweeter.server.dto.UserDTO;

public class DatabaseFiller {
    private static final int NUM_USERS = 10000;
    private static final String FOLLOWEE_ALIAS = "@jt";
    private static final String FOLLOWEE_NAME = "James Talmage";
    private static final String FOLLOWEE_IMAGE = "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40jt";

    private final IUserDAO userDAO;
    private final IFollowDAO followDAO;
    private final IFeedDAO feedDAO;

    private List<FollowDTO> followers;
    private List<UserDTO> users;
    private List<String> receivers;

    public DatabaseFiller(DAOFactory factory) {
        userDAO = factory.getUserDAO();
        followDAO = factory.getFollowDAO();
        feedDAO = factory.getFeedDAO();
    }

    private void createUserList() {
        if (users == null) {
            users = new ArrayList<>();
            for (int i = 1; i <= NUM_USERS; i++) {
                String name = "Guy " + i;
                String alias = "@guy" + i;

                UserDTO user = new UserDTO();
                user.setAlias(alias);
                user.setFirstName(name);
                users.add(user);
            }
        }
    }

    private void createFollowersList() {
        if (followers == null) {
            followers = new ArrayList<>();
            for (int i = 1; i <= NUM_USERS; i++) {
                String name = "Guy " + i;
                String alias = "@guy" + i;

                FollowDTO follow = new FollowDTO();
                follow.setFollow_handle(alias);
                follow.setFollow_name(name);
                follow.setFollowee_handle(FOLLOWEE_ALIAS);
                follow.setFollowee_name(FOLLOWEE_NAME);
                follow.setFollowee_image(FOLLOWEE_IMAGE);
                followers.add(follow);
            }
        }
    }

    private void createReceiverList() {
        if (receivers == null) {
            receivers = new ArrayList<>();
            for (int i = 1; i <= NUM_USERS; i++) {
                String alias = "@guy" + i;
                receivers.add(alias);
            }
        }
    }

    public void addUsers() {
        createUserList();
        userDAO.addUserBatch(users);
    }

    public void addFollowers() {
        createFollowersList();
        followDAO.addFollowersBatch(followers);
    }

    public void clearUsers() {
        createUserList();
        userDAO.deleteAllUsers(users);
    }

    public void clearFollowers() {
        createFollowersList();
        followDAO.deleteAllFollowers(followers);
    }

    public void clearFeed(List<Long> timestamps) {
        createReceiverList();
        for (Long timestamp : timestamps) {
            feedDAO.clearFeed(receivers, timestamp);
        }
    }
}
