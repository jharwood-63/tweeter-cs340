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

public class MainPresenter {
    private static final String LOG_TAG = "MainActivity";

    public interface View {
        void displayMessage(String message);

        void isFollower(boolean isFollower);

        void updateSelectedUserFollowingAndFollowers();

        void updateFollowButton(boolean value);

        void setFollowButtonEnabled(boolean value);

        void logoutUser();

        void cancelPostToast();

        void displayFollowerCount(int count);

        void displayFollowingCount(int count);
    }

    private View view;

    private FollowService followService;

    private UserService userService;

    private StatusService statusService;

    public MainPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
        statusService = new StatusService();
    }

    public void checkIsFollower(User selectedUser) {
        followService.isFollower(selectedUser, new IsFollowerObserver());
    }

    public void unfollow(User selectedUser) {
        view.displayMessage("Removing " + selectedUser.getName() + "...");
        followService.unfollow(selectedUser, new UpdateFollowStatusObserver());
    }

    public void follow(User selectedUser) {
        view.displayMessage("Adding " + selectedUser.getName() + "...");
        followService.follow(selectedUser, new UpdateFollowStatusObserver());
    }

    public void updateFollowingAndFollowers(User selectedUser) {
        followService.updateFollowersCount(selectedUser, new UpdateCountObserver());
        followService.updateFollowingCount(selectedUser, new UpdateCountObserver());
    }

    public void logout() {
        userService.logout(new LogoutObserver());
    }

    public void postStatus(String post) {
        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            statusService.postStatus(newStatus, new PostObserver());
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            view.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    // Helper Functions
    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
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

    public int findUrlEndIndex(String word) {
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

    public List<String> parseMentions(String post) {
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
    private class IsFollowerObserver implements AuthenticatedNotificationObserver<Boolean> {

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(Boolean isFollower) {
            view.isFollower(isFollower);
        }
    }

    private class UpdateFollowStatusObserver implements SimpleNotificationObserver {

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
            view.setFollowButtonEnabled(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
            view.setFollowButtonEnabled(true);
        }

        @Override
        public void handleSuccess() {
            view.updateSelectedUserFollowingAndFollowers();
            view.updateFollowButton(false);
            view.setFollowButtonEnabled(true);
        }
    }

    private class UpdateCountObserver implements AuthenticatedNotificationObserver<Integer> {

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(Integer count) {
            view.displayFollowerCount(count);
            view.displayFollowingCount(count);
        }
    }

    private class LogoutObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }


        @Override
        public void handleSuccess() {
            view.logoutUser();
        }
    }

    private class PostObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to post status because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.cancelPostToast();
            view.displayMessage("Successfully Posted!");
        }
    }
}
