package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter {
    private static final int PAGE_SIZE = 10;
    public interface View {
        void displayMessage(String message);

        void showUser(User user);

        void setLoadingFooter(boolean value);

        void addMoreItems(List<Status> statuses);
    }

    private View view;

    private UserService userService;

    private StatusService statusService;

    private Status lastStatus;

    private boolean hasMorePages;

    private boolean isLoading = false;

    public GetFeedPresenter(View view) {
        this.view = view;
        userService = new UserService();
        statusService = new StatusService();
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void getUser(String userAlias) {
        userService.getUser(userAlias, new GetUserObserver());
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(isLoading);
            statusService.getFeed(user, PAGE_SIZE, lastStatus, new GetFeedObserver());
        }
    }

    private class GetUserObserver implements UserService.Observer {
        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void showUser(User user) {
            view.showUser(user);
        }
    }

    private class GetFeedObserver implements StatusService.Observer {

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get feed: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }

        @Override
        public void addStatuses(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(statuses);
        }
    }
}
