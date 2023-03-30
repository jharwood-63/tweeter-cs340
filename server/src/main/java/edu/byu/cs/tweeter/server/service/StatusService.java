package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.IStoryDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends Service {
    private IStoryDAO storyDAO;
    private IFeedDAO feedDAO;

    private IStoryDAO getStoryDAO() {
        if (storyDAO == null) {
            storyDAO = getFactory().getStatusDAO();
        }

        return storyDAO;
    }

    private IFeedDAO getFeedDAO() {
        if (feedDAO == null) {
            feedDAO = getFactory().getFeedDAO();
        }

        return feedDAO;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getStatus().getPost().equals("") || request.getStatus().getPost() == null) {
            throw new RuntimeException("[Bad Request] Request must have a post");
        }

        long currentTime = System.currentTimeMillis();
        IFollowDAO followDAO = getFactory().getFollowDAO();
        List<User> followers = followDAO.getAllFollowers(request.getStatus().getUser().getAlias());

        getFeedDAO().postStatusToFeed(request, currentTime, followers);
        getStoryDAO().postStatusToStory(request, currentTime);

        return new PostStatusResponse(true);
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getUserAlias().equals("") || request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request must have a post");
        }
        else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Request must have a limit greater than 0");
        }

        return getFeedDAO().getFeed(request);
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
        if (!getAuthTokenDAO().authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getUserAlias().equals("") || request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request must have a user");
        }
        else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Request must have a limit greater than 0");
        }

        return getStoryDAO().getStory(request);
    }
}
