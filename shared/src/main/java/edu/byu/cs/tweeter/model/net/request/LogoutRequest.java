package edu.byu.cs.tweeter.model.net.request;

public class LogoutRequest {
    private String token;

    private LogoutRequest() {}

    public LogoutRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
