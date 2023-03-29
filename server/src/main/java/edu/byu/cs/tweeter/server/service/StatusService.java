package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.IStatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.StatusDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {
    private final IStatusDAO statusDAO;

    public StatusService(IStatusDAO statusDAO) {
        this.statusDAO = statusDAO;
    }

    protected IStatusDAO getStatusDAO() {
        return statusDAO;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getStatus().getPost().equals("") || request.getStatus().getPost() == null) {
            throw new RuntimeException("[Bad Request] Request must have a post");
        }

        return new PostStatusResponse(true);
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getUserAlias().equals("") || request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request must have a post");
        }
        else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Request must have a limit greater than 0");
        }

        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new GetFeedResponse(data.getSecond(), data.getFirst());
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getUserAlias().equals("") || request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request must have a user");
        }
        else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Request must have a limit greater than 0");
        }

        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new GetStoryResponse(data.getSecond(), data.getFirst());
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
