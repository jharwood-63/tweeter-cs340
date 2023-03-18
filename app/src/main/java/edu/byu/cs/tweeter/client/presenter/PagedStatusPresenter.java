package edu.byu.cs.tweeter.client.presenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedStatusPresenter extends PagedPresenter<Status> {
    protected StatusService statusService;

    public PagedStatusPresenter(PagedView<Status> view) {
        super(view);
        statusService = new StatusService();
    }
}
