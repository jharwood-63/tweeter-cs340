package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter extends PagedUserPresenter {
    public GetFollowingPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    protected String getExceptionPrefix() {
        return "Failed to get following because of exception: ";
    }

    @Override
    protected String getFailurePrefix() {
        return "Failed to get following: ";
    }
}
