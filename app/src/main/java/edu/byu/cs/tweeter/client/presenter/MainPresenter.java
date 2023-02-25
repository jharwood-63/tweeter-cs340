package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {
    private static final String LOG_TAG = "MainActivity";

    public interface MainView extends PresenterView {
        void isFollower(boolean isFollower);

        void updateSelectedUserFollowingAndFollowers();

        void updateFollowButton(boolean value);

        void setFollowButtonEnabled(boolean value);

        void logoutUser();

        void cancelPostToast();

        void displayFollowerCount(int count);

        void displayFollowingCount(int count);
    }
    
    private FollowService followService;

    private UserService userService;

    private StatusService statusService;

    public MainPresenter(MainView view) {
        super(view);
        followService = new FollowService();
        userService = new UserService();
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }

        return statusService;
    }
    
    protected MainView getMainView() {
        return ((MainView) getView());
    }

    public void checkIsFollower(User selectedUser) {
        followService.isFollower(selectedUser, new IsFollowerObserver());
    }

    public void unfollow(User selectedUser) {
        getMainView().displayMessage("Removing " + selectedUser.getName() + "...");
        followService.unfollow(selectedUser, new UpdateFollowStatusObserver());
    }

    public void follow(User selectedUser) {
        getMainView().displayMessage("Adding " + selectedUser.getName() + "...");
        followService.follow(selectedUser, new UpdateFollowStatusObserver());
    }

    public void updateFollowingAndFollowers(User selectedUser) {
        followService.updateCount(selectedUser, new UpdateCountObserver());
    }

    public void logout() {
        userService.logout(new LogoutObserver());
    }

    public void postStatus(String post) {
        getMainView().displayMessage("Posting status...");

        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            getStatusService().postStatus(newStatus, new PostObserver());
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            getMainView().displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    // Helper Functions
    private String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    private List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    private List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    // Observers
    private class UpdateFollowStatusObserver implements SimpleNotificationObserver {

        private void unsuccessfulTaskCompletion(String message) {
            getMainView().displayMessage(message);
            getMainView().setFollowButtonEnabled(true);
        }

        @Override
        public void handleException(Exception ex) {
            unsuccessfulTaskCompletion("Failed to unfollow because of exception: " + ex.getMessage());
        }

        @Override
        public void handleFailure(String message) {
            unsuccessfulTaskCompletion(message);
        }

        @Override
        public void handleSuccess() {
            getMainView().updateSelectedUserFollowingAndFollowers();
            getMainView().updateFollowButton(false);
            getMainView().setFollowButtonEnabled(true);
        }
    }

    private class IsFollowerObserver extends PresenterObserver implements AuthenticatedNotificationObserver<Boolean> {
        @Override
        protected String getMessagePrefix() {
            return "Failed to determine following relationship: ";
        }

        @Override
        protected String getExceptionPrefix() {
            return "Failed to determine following relationship because of exception: ";
        }

        @Override
        public void handleSuccess(Boolean isFollower) {
            getMainView().isFollower(isFollower);
        }
    }

    private class UpdateCountObserver extends PresenterObserver implements AuthenticatedNotificationObserver<Integer> {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get followers count: ";
        }

        @Override
        protected String getExceptionPrefix() {
            return "Failed to get followers count because of exception: ";
        }

        @Override
        public void handleSuccess(Integer count) {
            getMainView().displayFollowerCount(count);
            getMainView().displayFollowingCount(count);
        }
    }

    private class LogoutObserver extends PresenterObserver implements SimpleNotificationObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to logout: ";
        }

        @Override
        protected String getExceptionPrefix() {
            return "Failed to logout because of exception: ";
        }

        @Override
        public void handleSuccess() {
            getMainView().logoutUser();
        }
    }

    protected class PostObserver extends PresenterObserver implements SimpleNotificationObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to post status: ";
        }

        @Override
        protected String getExceptionPrefix() {
            return "Failed to post status because of exception: ";
        }

        @Override
        public void handleSuccess() {
            getMainView().cancelPostToast();
            getMainView().displayMessage("Successfully Posted!");
        }
    }
}
