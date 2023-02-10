package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public interface Observer {

        void displayError(String message);

        void displayException(Exception ex);

        void addStatuses(List<Status> statuses, boolean hasMorePages);
    }

    public interface PostObserver {
        void displayMessage(String message);

        void cancelToast();
    }

    public void getFeed(User user, int pageSize, Status lastStatus, Observer observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new GetFeedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    public void getStory(User user, int pageSize, Status lastStatus, Observer observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new GetStoryHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    public void postStatus(Status newStatus, PostObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }
}
