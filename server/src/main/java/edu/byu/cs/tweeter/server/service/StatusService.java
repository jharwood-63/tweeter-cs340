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
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.IStoryDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends Service {
    private IStoryDAO storyDAO;
    private IFeedDAO feedDAO;
    private final DAOFactory factory;

    public StatusService(DAOFactory factory) {
        this.factory = factory;
    }

    private IStoryDAO getStoryDAO() {
        if (storyDAO == null) {
            storyDAO = factory.getStatusDAO();
        }

        return storyDAO;
    }

    private IFeedDAO getFeedDAO() {
        if (feedDAO == null) {
            feedDAO = factory.getFeedDAO();
        }

        return feedDAO;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (!getAuthTokenDAO(factory).authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getStatus().getPost().equals("") || request.getStatus().getPost() == null) {
            throw new RuntimeException("[Bad Request] Request must have a post");
        }

        // All of the follow and feed stuff will get moved somewhere else
        // All that will be done here is posting the status to the story and putting the request on the queue
        // Then it will return
        long currentTime = System.currentTimeMillis();
        IFollowDAO followDAO = factory.getFollowDAO();
        List<User> followers = followDAO.getAllFollowers(request.getStatus().getUser().getAlias());
        getFeedDAO().postStatusToFeed(request, currentTime, followers);

        // Do this and then...
        getStoryDAO().postStatusToStory(request, currentTime);
        // write to the queue and then...
        // return true
        return new PostStatusResponse(true);
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if (!getAuthTokenDAO(factory).authenticateRequest(request.getAuthToken())) {
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
        if (!getAuthTokenDAO(factory).authenticateRequest(request.getAuthToken())) {
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
