package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public interface Observer {

        void displayError(String message);

        void displayException(Exception ex);

        void addFollowees(List<User> followees, boolean hasMorePages);
    }

    public interface IsFollowerObserver {

        void displayError(String message);

        void displayException(Exception ex);

        void isFollower(boolean isFollower);
    }

    public interface UpdateFollowStatusObserver {
        void displayMessage(String message);

        void updateSelectedUserFollowingAndFollowers();

        void updateFollowButton(boolean value);

        void setFollowButtonEnabled(boolean value);
    }

    public interface UpdateCountObserver {
        void displayMessage(String message);

        void displayFollowerCount(int count);

        void displayFollowingCount(int count);
    }

    public void getFollowees(User user, int pageSize, User lastFollowee, Observer observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void getFollowers(User user, int pageSize, User lastFollower, Observer observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void isFollower(User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void unfollow(User selectedUser, UpdateFollowStatusObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);

        observer.displayMessage("Removing " + selectedUser.getName() + "...");
    }

    public void follow(User selectedUser, UpdateFollowStatusObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);

        observer.displayMessage("Adding " + selectedUser.getName() + "...");
    }

    public void updateFollowingAndFollowers(User selectedUser, UpdateCountObserver observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        updateFollowerCount(selectedUser, executor, observer);

        updateFollowingCount(selectedUser, executor, observer);
    }

    private void updateFollowerCount(User selectedUser, ExecutorService executor, UpdateCountObserver observer) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);
    }

    private void updateFollowingCount(User selectedUser, ExecutorService executor, UpdateCountObserver observer) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends Handler {
        private Observer observer;
        public GetFollowingHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.addFollowees(followees, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.displayError("Failed to get following: " + message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {

        private Observer observer;

        public GetFollowersHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                observer.addFollowees(followers, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.displayError("Failed to get followers: " + message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    // IsFollowerHandler
    private class IsFollowerHandler extends Handler {

        private IsFollowerObserver observer;

        public IsFollowerHandler(IsFollowerObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                observer.isFollower(isFollower);
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.displayError("Failed to determine following relationship: " + message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    // UnfollowHandler
    private class UnfollowHandler extends Handler {

        private UpdateFollowStatusObserver observer;

        public UnfollowHandler(UpdateFollowStatusObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                observer.updateSelectedUserFollowingAndFollowers();
                observer.updateFollowButton(true);
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                observer.displayMessage("Failed to unfollow: " + message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                observer.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
            }

            observer.setFollowButtonEnabled(true);
        }
    }

    // FollowHandler
    private class FollowHandler extends Handler {

        private UpdateFollowStatusObserver observer;

        public FollowHandler(UpdateFollowStatusObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                observer.updateSelectedUserFollowingAndFollowers();
                observer.updateFollowButton(false);
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                observer.displayMessage("Failed to follow: " + message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                observer.displayMessage("Failed to follow because of exception: " + ex.getMessage());
            }

            observer.setFollowButtonEnabled(true);
        }
    }

    // GetFollowersCountHandler
    private class GetFollowersCountHandler extends Handler {

        private UpdateCountObserver observer;

        public GetFollowersCountHandler(UpdateCountObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                observer.displayFollowerCount(count);
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                observer.displayMessage("Failed to get followers count: " + message);
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                observer.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());
            }
        }
    }

    // GetFollowingCountHandler
    private class GetFollowingCountHandler extends Handler {

        private UpdateCountObserver observer;

        public GetFollowingCountHandler(UpdateCountObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                observer.displayFollowingCount(count);
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                observer.displayMessage("Failed to get following count: " + message);
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                observer.displayMessage("Failed to get following count because of exception: " + ex.getMessage());
            }
        }
    }
}
