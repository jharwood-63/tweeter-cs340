package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    public interface View {
        void displayMessage(String message);

        void isFollower(boolean isFollower);

        void updateSelectedUserFollowingAndFollowers();

        void updateFollowButton(boolean value);

        void setFollowButtonEnabled(boolean value);

        void logoutUser();
    }

    private View view;

    private FollowService followService;

    private UserService userService;

    public MainPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    public void checkIsFollower(User selectedUser) {
        followService.isFollower(selectedUser, new IsFollowerObserver());
    }

    public void unfollow(User selectedUser) {
        followService.unfollow(selectedUser, new UpdateFollowStatusObserver());
    }

    public void follow(User selectedUser) {
        followService.follow(selectedUser, new UpdateFollowStatusObserver());
    }

    public void logout() {
        userService.logout(new LogoutObserver());
    }

    private class IsFollowerObserver implements FollowService.IsFollowerObserver{

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }

        @Override
        public void isFollower(boolean isFollower) {
            view.isFollower(isFollower);
        }
    }

    private class UpdateFollowStatusObserver implements FollowService.UpdateFollowStatusObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void updateSelectedUserFollowingAndFollowers() {
            view.updateSelectedUserFollowingAndFollowers();
        }

        @Override
        public void updateFollowButton(boolean value) {
            view.updateFollowButton(value);
        }

        @Override
        public void setFollowButtonEnabled(boolean value) {
            view.setFollowButtonEnabled(value);
        }
    }

    private class LogoutObserver implements UserService.LogoutObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void logoutUser() {
            view.logoutUser();
        }
    }
}
