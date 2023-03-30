package edu.byu.cs.tweeter.server.dao.dynamodb;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.IStatusDAO;

public class StatusDAO implements IStatusDAO {
    @Override
    public PostStatusResponse postStatus(PostStatusRequest request) {
        return null;
    }

    @Override
    public GetFeedResponse getFeed(GetFeedRequest request) {
        return null;
    }

    @Override
    public GetStoryResponse getStory(GetStoryRequest request) {
        return null;
    }

    @Override
    public boolean authenticateRequest(AuthToken authToken) {
        return false;
    }
}
