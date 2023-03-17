package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getFollowingDAO().getFollowees(request);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }

        return new UnfollowResponse(true);
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }

        return new FollowResponse(true);
    }

    public GetCountResponse getFollowersCount(GetCountRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request must have a user");
        }
        else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request must have an authtoken");
        }

        int count = getFollowingDAO().getFollowersCount(request.getUser());

        return new GetCountResponse(count, true);
    }

    public GetCountResponse getFollowingCount(GetCountRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request must have a user");
        }
        else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request must have an authtoken");
        }

        int count = getFollowingDAO().getFollowingCount(request.getUser());

        return new GetCountResponse(count, true);
    }
}
