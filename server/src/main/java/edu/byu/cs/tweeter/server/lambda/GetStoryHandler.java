package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler extends BaseStatusHandler implements RequestHandler<GetStoryRequest, GetStoryResponse> {
    @Override
    public GetStoryResponse handleRequest(GetStoryRequest input, Context context) {
        return getService().getStory(input);
    }
}
