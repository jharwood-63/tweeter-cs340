package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest extends AuthenticatedRequest {
    //FIXME: DONT FORGET TO UPDATE THE MODEL IN AWS
    private User followee;
    private User follower;
    private FollowRequest() {}

    public FollowRequest(AuthToken authToken, User followee, User follower) {
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
