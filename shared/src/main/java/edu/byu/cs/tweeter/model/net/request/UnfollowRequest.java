package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest extends AuthenticatedRequest {
    //FIXME: REMEMBER TO UPDATE THE MODEL IN AWS API GATEWAY
    private User followee; // person being unfollowed
    private User follower; // person that is doing the unfollowing
    private UnfollowRequest() {}

    public UnfollowRequest(AuthToken authToken, User followee, User follower) {
        super(authToken);
        this.followee = followee;
        this.follower = follower;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }
}
