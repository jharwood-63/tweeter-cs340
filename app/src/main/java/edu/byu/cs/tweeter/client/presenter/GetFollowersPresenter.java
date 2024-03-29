package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
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
    protected String getMessagePrefix() {
        return "Failed to get followers: ";
    }

    @Override
    protected void getItems(User user, int pageSize, PagedNotificationObserver<User> getItemsObserver) {
        followService.getFollowers(user, PAGE_SIZE, getLastItem(), getItemsObserver);
    }
}
