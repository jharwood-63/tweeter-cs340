package edu.byu.cs.tweeter.server.service;

import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.FollowDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends Service {
    private IFollowDAO followDAO;
    private IUserDAO userDAO;

    private IFollowDAO getFollowDAO() {
        if (followDAO == null) {
            followDAO = getFactory().getFollowDAO();
        }

        return followDAO;
    }

    private IUserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = getFactory().getUserDAO();
        }

        return userDAO;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public GetFollowingResponse getFollowing(GetFollowingRequest request) {
        if(request.getFollowerAlias() == null || request.getFollowerAlias().equals("")) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        else if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }

        return getFollowDAO().getFollowing(request);
    }

    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
        if(request.getFolloweeAlias() == null || request.getFolloweeAlias().equals("")) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        return getFollowDAO().getFollowers(request);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }

        getFollowDAO().unfollow(request);
        // -1 from followee's followers count
        // -1 from follower's following count
        getUserDAO().decrementFollowingCount(request.getFollower().getAlias());
        getUserDAO().decrementFollowersCount(request.getFollowee().getAlias());

        return new UnfollowResponse(true);
    }

    public FollowResponse follow(FollowRequest request) {
        if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }

        getFollowDAO().follow(request);
        // +1 to followee's followers count
        // +1 to follower's following count
        getUserDAO().incrementFollowingCount(request.getFollower().getAlias());
        getUserDAO().incrementFollowersCount(request.getFollowee().getAlias());

        return new FollowResponse(true);
    }

    public GetCountResponse getFollowersCount(GetCountRequest request) {
        if (request.getUserAlias() == null || request.getUserAlias().equals("")) {
            throw new RuntimeException("[Bad Request] Request must have a user");
        }
        else if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }

        int count = getUserDAO().getFollowersCount(request.getUserAlias());

        return new GetCountResponse(count, true);
    }

    public GetCountResponse getFollowingCount(GetCountRequest request) {
        if (request.getUserAlias() == null || request.getUserAlias().equals("")) {
            throw new RuntimeException("[Bad Request] Request must have a user");
        }
        else if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }

        int count = getUserDAO().getFollowingCount(request.getUserAlias());

        return new GetCountResponse(count, true);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getFolloweeAlias() == null || request.getFolloweeAlias().equals("")) {
            throw new RuntimeException("[Bad Request] Request must have a followee");
        }
        else if (request.getFollowerAlias() == null || request.getFollowerAlias().equals("")) {
            throw new RuntimeException("[Bad Request] Request must have a follower");
        }
        else if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }

        boolean isFollower = getFollowDAO().isFollower(request);

        return new IsFollowerResponse(true, isFollower);
    }
}
