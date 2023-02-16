package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter extends PagedUserPresenter {
    public GetFollowersPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    protected String getExceptionPrefix() {
        return "Failed to get followers because of exception: ";
    }

    @Override
    protected String getFailurePrefix() {
        return "Failed to get followers: ";
    }
}
