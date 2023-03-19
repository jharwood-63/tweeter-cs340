package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest {
    /*
    * Using JUnit and Mockito, write an INTEGRATION test for your client-side Service that returns a
    * user's story pages (i.e., StatusService). The Service should have an operation that creates a
    * background task to retrieve a user's story from the server and notifies the Service's observer
    * of the operation's outcome. Your test should verify that the Service's observer is notified in
    * the case of a successful story retrieval.  Note: you do NOT have to write tests for the other
    * outcomes (i.e., failing not because of an exception, failing because of an exception).
     */

    private User currentUser;
    private AuthToken currAuthToken; //Use Mockito to mock the cache
    private Cache mockCache;

    private StatusService statusServiceSpy;
    private StatusServiceObserver observer;

    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        currentUser = new User("first", "last", null);
        currAuthToken = new AuthToken("Test token");

        mockCache = Mockito.mock(Cache.class);
        Cache.setInstance(mockCache);
        Mockito.when(mockCache.getCurrUserAuthToken()).thenReturn(currAuthToken);

        statusServiceSpy = Mockito.spy(new StatusService());

        observer = new StatusServiceObserver();

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    private class StatusServiceObserver implements PagedNotificationObserver<Status> {

        private boolean success;
        private String message;
        private List<Status> story;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List<Status> story, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.story = story;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.story = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.success = false;
            this.message = null;
            this.story = null;
            this.hasMorePages = false;
            this.exception = exception;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStory() {
            return story;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws InterruptedException {
        statusServiceSpy.getStory(currentUser, 3, null, observer);
        awaitCountDownLatch();

        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        Assertions.assertNotNull(observer.getStory());
        Assertions.assertNotNull(observer.getStory().get(0).getPost());
        Assertions.assertTrue(observer.getHasMorePages());
        Assertions.assertNull(observer.getException());
    }
}
