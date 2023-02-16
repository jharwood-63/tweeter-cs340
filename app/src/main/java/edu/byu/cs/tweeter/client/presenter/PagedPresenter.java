package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class PagedPresenter<T> extends Presenter {
    protected static final int PAGE_SIZE = 10;

    public interface PagedView<T> extends PresenterView {
        void showUser(User user);

        void setLoadingFooter(boolean value);

        void addMoreItems(List<T> items);
    }

    private PagedView<T> view;

    private UserService userService;

    private boolean hasMorePages;

    private boolean isLoading = false;

    public PagedPresenter(PagedView<T> view) {
        this.view = view;
        this.userService = new UserService();
    }

    public UserService getUserService() {
        return userService;
    }

    public PagedView<T> getView() {
        return view;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean value) {
        isLoading = value;
    }

    public void setHasMorePages(boolean value) {
        hasMorePages = value;
    }

    public void setLoadingFooter(boolean value) {
        setLoading(value);
        getView().setLoadingFooter(value);
    }

    public void getUser(String userAlias) {
        getView().displayMessage("Getting user's profile...");
        getUserService().getUser(userAlias, new GetUserObserver());
    }

    private class GetUserObserver implements AuthenticatedNotificationObserver<User> {
        @Override
        public void handleFailure(String message) {
            getView().displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            getView().displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User user) {
            getView().showUser(user);
        }
    }
}