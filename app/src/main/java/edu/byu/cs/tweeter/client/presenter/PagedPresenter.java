package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter implements PagedNotificationObserver<T> {
    protected static final int PAGE_SIZE = 10;

    public interface PagedView<T> extends PresenterView {
        void showUser(User user);

        void setLoadingFooter(boolean value);

        void addMoreItems(List<T> items);
    }

    private UserService userService;

    private T lastItem;

    private boolean hasMorePages;

    private boolean isLoading = false;

    public PagedPresenter(PagedView<T> view) {
        super(view);
        this.userService = new UserService();
    }

    protected PagedView<T> getPagedView() {
        return ((PagedView<T>) getView());
    }

    public UserService getUserService() {
        return userService;
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

    public T getLastItem() {
        return lastItem;
    }

    public void setLoadingFooter(boolean value) {
        setLoading(value);
        getPagedView().setLoadingFooter(value);
    }

    public void getUser(String userAlias) {
        getView().displayMessage("Getting user's profile...");
        getUserService().getUser(userAlias, new GetUserObserver(null));
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoadingFooter(true);
            getItem(user, PAGE_SIZE, this);
        }
    }

    protected abstract void getItem(User user, int pageSize, PagedNotificationObserver<T> getItemsObserver);

    @Override
    public void handleSuccess(List<T> items, boolean hasMoreItems) {
        setLoadingFooter(false);
        lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        setHasMorePages(hasMorePages);
        getPagedView().addMoreItems(items);
    }

    @Override
    public void handleFailure(String message) {
        setLoadingFooter(false);
        getView().displayMessage(getMessagePrefix() + message);
    }

    protected abstract String getMessagePrefix();

    @Override
    public void handleException(Exception ex) {
        setLoadingFooter(false);
        getView().displayMessage(getExceptionPrefix() + ex.getMessage());
    }

    protected abstract String getExceptionPrefix();

    private class GetUserObserver extends Presenter implements AuthenticatedNotificationObserver<User> {
        public GetUserObserver(PresenterView view) {
            super(view);
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to get user's profile: ";
        }

        @Override
        protected String getExceptionPrefix() {
            return "Failed to get user's profile because of exception: ";
        }

        @Override
        public void handleSuccess(User user) {
            getPagedView().showUser(user);
        }
    }
}