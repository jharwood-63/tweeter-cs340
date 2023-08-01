package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dto.FeedDTO;

public interface IFeedDAO {
    void postStatusToFeed(PostStatusRequest request, long currentTime, List<User> followers);
    GetFeedResponse getFeed(GetFeedRequest request);
    void clearFeed(List<String> receivers, long timestamp);
}
