package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowListTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.CountNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service {
    public void getFollowList(User user, int pageSize, User lastFollowee, PagedNotificationObserver<User> observer) {
        GetFollowListTask getFollowingTask = new GetFollowListTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new PagedNotificationHandler<>(observer));
        runTask(getFollowingTask);
    }

    public void unfollow(User selectedUser, SimpleNotificationObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        runTask(unfollowTask);
    }

    public void follow(User selectedUser, SimpleNotificationObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        runTask(followTask);
    }

    public void updateCount(User selectedUser, AuthenticatedNotificationObserver<Integer> observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new CountNotificationHandler(observer));

        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new CountNotificationHandler(observer));

        runTask(followersCountTask);
        runTask(followingCountTask);
    }

    public void isFollower(User selectedUser, AuthenticatedNotificationObserver<Boolean> observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        runTask(isFollowerTask);
    }
}
