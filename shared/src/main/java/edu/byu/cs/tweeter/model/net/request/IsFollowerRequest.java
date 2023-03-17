package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class IsFollowerRequest {
    private String token;
    private String followerAlias; // This is the person you are checking
    private String followeeAlias; // This is you

    public IsFollowerRequest() {}

    public IsFollowerRequest(String token, String followerAlias, String followeeAlias) {
        this.token = token;
        this.followerAlias = followerAlias;
        this.followeeAlias = followeeAlias;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
}
