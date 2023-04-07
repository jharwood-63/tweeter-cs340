package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dto.FollowDTO;

public interface IFollowDAO {
    GetFollowingResponse getFollowing(GetFollowingRequest request);
    GetFollowersResponse getFollowers(GetFollowersRequest request);
//    List<User> getFollowersForFeedUpdateQueue(String senderAlias);
    void unfollow(UnfollowRequest request);
    void follow(FollowRequest request);
    boolean isFollower(IsFollowerRequest request);
    void addFollowersBatch(List<FollowDTO> followers);
    void deleteAllFollowers(List<FollowDTO> followers);
}
