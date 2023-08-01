package edu.byu.cs.tweeter.server.queue;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class UpdateFeedQueue extends PostStatusQueue {
    private List<User> followers;

    private UpdateFeedQueue() {}

    public UpdateFeedQueue(User sender, Status post, long currentTime, List<User> followers) {
        super(sender, post, currentTime);
        this.followers = followers;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }
}
