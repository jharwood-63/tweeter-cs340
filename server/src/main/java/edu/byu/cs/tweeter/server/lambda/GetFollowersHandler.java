package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler extends BaseFollowHandler implements RequestHandler<GetFollowersRequest, GetFollowersResponse> {
    @Override
    public GetFollowersResponse handleRequest(GetFollowersRequest input, Context context) {
        return getService().getFollowers(input);
    }
}
