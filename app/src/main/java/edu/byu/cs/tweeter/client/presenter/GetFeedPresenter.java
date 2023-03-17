package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter extends PagedStatusPresenter {
    public GetFeedPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    protected String getExceptionPrefix() {
        return "Failed to get feed because of exception: ";
    }

    @Override
    protected String getMessagePrefix() {
        return "Failed to get feed: ";
    }

    @Override
    protected void getItems(User user, int pageSize, PagedNotificationObserver<Status> getItemsObserver) {
        statusService.getFeed(user, PAGE_SIZE, getLastItem(), getItemsObserver);
    }
}
