package edu.byu.cs.tweeter.model.net.response;

public class GetCountResponse extends Response {
    private int count;

    // Success
    public GetCountResponse(int count, boolean success) {
        super(success);
        this.count = count;
    }

    // Failure
    public GetCountResponse(boolean success, String message) {
        super(success, message);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
