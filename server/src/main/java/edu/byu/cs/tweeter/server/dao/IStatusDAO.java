package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public interface IStatusDAO {
    PostStatusResponse postStatus(PostStatusRequest request);
    GetFeedResponse getFeed(GetFeedRequest request);
    GetStoryResponse getStory(GetStoryRequest request);
    boolean authenticateRequest(AuthToken authToken);
}
