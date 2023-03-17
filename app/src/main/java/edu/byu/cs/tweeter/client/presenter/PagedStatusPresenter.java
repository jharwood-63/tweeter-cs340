package edu.byu.cs.tweeter.client.presenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public String getFormattedDate(String timestamp){
        return new SimpleDateFormat("E MMM d k:mm:ss z y", Locale.US).format(new Date(timestamp));
    }
}
