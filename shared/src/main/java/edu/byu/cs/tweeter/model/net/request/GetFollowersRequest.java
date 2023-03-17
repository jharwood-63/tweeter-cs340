package edu.byu.cs.tweeter.model.net.request;

public class GetFollowersRequest {
    private String token;
    private String followeeAlias;
    private int limit;
    private String lastFollowerAlias;

    public GetFollowersRequest() {}

    public GetFollowersRequest(String token, String followeeAlias, int limit, String lastFollowerAlias) {
        this.token = token;
        this.followeeAlias = followeeAlias;
        this.limit = limit;
        this.lastFollowerAlias = lastFollowerAlias;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }

    public void setLastFollowerAlias(String lastFollowerAlias) {
        this.lastFollowerAlias = lastFollowerAlias;
    }
}
