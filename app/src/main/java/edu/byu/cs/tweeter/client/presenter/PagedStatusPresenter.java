package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedStatusPresenter extends PagedPresenter<Status> {
    private StatusService statusService;

    private Status lastStatus;

    public PagedStatusPresenter(PagedView<Status> view) {
        super(view);
        statusService = new StatusService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoadingFooter(true);
            statusService.getStatus(user, PAGE_SIZE, lastStatus, new GetStatusObserver());
        }
    }

    public abstract String getExceptionPrefix();

    public abstract String getFailurePrefix();

    private class GetStatusObserver implements PagedNotificationObserver<Status> {

        @Override
        public void handleFailure(String message) {
            setLoadingFooter(false);
            getView().displayMessage(getFailurePrefix() + message);
        }

        @Override
        public void handleException(Exception ex) {
            setLoadingFooter(false);
            getView().displayMessage(getExceptionPrefix() + ex.getMessage());
        }

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            getView().addMoreItems(statuses);
        }
    }
}
