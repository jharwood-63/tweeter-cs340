package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.IStoryDAO;
import edu.byu.cs.tweeter.server.queue.PostStatusQueue;
import edu.byu.cs.tweeter.server.queue.UpdateFeedQueue;

public class StatusService extends Service {
    private static final String QUEUE_URL = "https://sqs.us-west-2.amazonaws.com/938404099055/PostStatusQueue";

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

    public PostStatusResponse postStatusToStory(PostStatusRequest request) {
        if (!getAuthTokenDAO(factory).authenticateRequest(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getStatus().getPost().equals("") || request.getStatus().getPost() == null) {
            throw new RuntimeException("[Bad Request] Request must have a post");
        }

        long currentTime = System.currentTimeMillis();
        // Post the status to the story
        getStoryDAO().postStatusToStory(request, currentTime);
        // Write to the queue post status queue
        PostStatusQueue queue = new PostStatusQueue(request.getStatus().getUser(), request.getStatus(), currentTime);
        sendMessageToQueue(queue, QUEUE_URL);

        // return true
        return new PostStatusResponse(true);
    }

    public void updateFeed(String messageBody) {
        //posts the status to the feed of the followers in each batch
        UpdateFeedQueue queueData = deserializeQueue(messageBody, UpdateFeedQueue.class);

        PostStatusRequest feedUpdateRequest = new PostStatusRequest(null, queueData.getPost());
        getFeedDAO().postStatusToFeed(feedUpdateRequest, queueData.getCurrentTime(), queueData.getFollowers());
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
