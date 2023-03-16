package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.getFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.getFollowersCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersCountHandler implements RequestHandler<getFollowersCountRequest, getFollowersCountResponse> {
    @Override
    public getFollowersCountResponse handleRequest(getFollowersCountRequest input, Context context) {
        FollowService followService = new FollowService();
        return followService.getFollowersCount(input);
    }
}
