package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
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
    protected String getMessagePrefix() {
        return "Failed to get following: ";
    }

    @Override
    protected void getItems(User user, int pageSize, PagedNotificationObserver<User> getItemsObserver) {
        followService.getFollowing(user, PAGE_SIZE, getLastItem(), getItemsObserver);
    }
}
