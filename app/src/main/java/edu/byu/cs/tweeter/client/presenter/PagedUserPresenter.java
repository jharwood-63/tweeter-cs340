package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedUserPresenter extends PagedPresenter<User> {
    private FollowService followService;

    public PagedUserPresenter(PagedView<User> view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void getItem(User user, int pageSize, PagedNotificationObserver<User> getItemsObserver) {
        followService.getFollowList(user, PAGE_SIZE, getLastItem(), getItemsObserver);
    }
}
