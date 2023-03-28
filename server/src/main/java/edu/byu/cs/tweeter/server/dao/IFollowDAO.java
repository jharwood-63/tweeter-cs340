package edu.byu.cs.tweeter.server.dao;

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

public interface IFollowDAO {
    int getFollowingCount(String userAlias);
    int getFollowersCount(String userAlias);
    GetFollowingResponse getFollowing(GetFollowingRequest request);
    GetFollowersResponse getFollowers(GetFollowersRequest request);
    UnfollowResponse unfollow(UnfollowRequest request);
    FollowResponse follow(FollowRequest request);
    IsFollowerResponse isFollower(IsFollowerRequest request);
}
