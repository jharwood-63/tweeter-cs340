package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;

public interface IStoryDAO {
    void postStatusToStory(PostStatusRequest request, long currentTime);
    GetStoryResponse getStory(GetStoryRequest request);
}
