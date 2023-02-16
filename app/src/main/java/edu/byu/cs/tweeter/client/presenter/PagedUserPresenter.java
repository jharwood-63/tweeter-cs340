package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedUserPresenter extends PagedPresenter<User> {
    private FollowService followService;

    private User lastFollower;

    public PagedUserPresenter(PagedView<User> view) {
        super(view);
        followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoadingFooter(true);
            followService.getFollowList(user, PAGE_SIZE, lastFollower, new GetFollowListObserver());
        }
    }

    protected abstract String getExceptionPrefix();

    protected abstract String getFailurePrefix();

    private class GetFollowListObserver implements PagedNotificationObserver<User> {
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
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            setLoadingFooter(false);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            getView().addMoreItems(followers);
        }
    }
}
