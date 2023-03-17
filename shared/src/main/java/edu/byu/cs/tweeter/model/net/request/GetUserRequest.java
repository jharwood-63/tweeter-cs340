package edu.byu.cs.tweeter.model.net.request;

public class GetUserRequest {
    private String token;
    private String alias;

    public GetUserRequest() {}

    public GetUserRequest(String token, String alias) {
        this.token = token;
        this.alias = alias;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
