package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UnfollowRequest extends AuthenticatedRequest {
    //FIXME: REMEMBER TO UPDATE THE MODEL IN AWS API GATEWAY
    private String followeeAlias; // person being unfollowed
    private String followerAlias; // person that is doing the unfollowing
    private UnfollowRequest() {}

    public UnfollowRequest(AuthToken authToken, String followeeAlias, String followerAlias) {
        super(authToken);
        this.followeeAlias = followeeAlias;
        this.followerAlias = followerAlias;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }
}
