package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UnfollowRequest extends AuthenticatedRequest {
    public UnfollowRequest() {}

    public UnfollowRequest(AuthToken authToken) {
        super(authToken);
    }
}
