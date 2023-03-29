package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowRequest extends AuthenticatedRequest {
    private FollowRequest() {}

    public FollowRequest(AuthToken authToken) {
        super(authToken);
    }
}
