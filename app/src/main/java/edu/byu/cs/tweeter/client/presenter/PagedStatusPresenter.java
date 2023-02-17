package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedStatusPresenter extends PagedPresenter<Status> {
    private StatusService statusService;

    public PagedStatusPresenter(PagedView<Status> view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void getItem(User user, int pageSize, PagedNotificationObserver<Status> getItemsObserver) {
        statusService.getStatus(user, PAGE_SIZE, getLastItem(), getItemsObserver);
    }
}
