package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {
    private boolean isFollower;

    public IsFollowerResponse(boolean success, boolean isFollower) {
        super(success);
        this.isFollower = isFollower;
    }

    public IsFollowerResponse(String message) {
        super(false, message);
    }

    public boolean getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(boolean isFollower) {
        this.isFollower = isFollower;
    }
}
