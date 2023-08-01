package edu.byu.cs.tweeter.server.queue;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class PostStatusQueue {
    private User sender;
    private Status post;
    private long currentTime;

    protected PostStatusQueue() {}

    public PostStatusQueue(User sender, Status post, long currentTime) {
        this.sender = sender;
        this.post = post;
        this.currentTime = currentTime;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Status getPost() {
        return post;
    }

    public void setPost(Status post) {
        this.post = post;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
