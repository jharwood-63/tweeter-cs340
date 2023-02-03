package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    public interface View {
        void displayMessage(String message);

        void isFollower(boolean isFollower);
    }

    private View view;

    private FollowService followService;

    public MainPresenter(View view) {
        this.view = view;
        followService = new FollowService();
    }

    public void checkIsFollower(User selectedUser) {
        followService.isFollower(selectedUser, new IsFollowerObserver());
    }

    private class IsFollowerObserver implements FollowService.MainObserver{

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
}
