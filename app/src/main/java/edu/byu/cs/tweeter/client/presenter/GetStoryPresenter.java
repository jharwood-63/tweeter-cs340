package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetStoryPresenter extends PagedStatusPresenter {
    public GetStoryPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    protected String getExceptionPrefix() {
        return "Failed to get story because of exception: ";
    }

    @Override
    protected String getMessagePrefix() {
        return "Failed to get story: ";
    }

    @Override
    protected void getItems(User user, int pageSize, PagedNotificationObserver<Status> getItemsObserver) {
        statusService.getStory(user, PAGE_SIZE, getLastItem(), getItemsObserver);
    }
}
