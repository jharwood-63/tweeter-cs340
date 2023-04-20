package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public class PostStatusIntegrationTest {
    private static final String LOGIN_URL = "login";
    private static final String GET_STORY_URL = "getstory";
    private static final String USER_ALIAS = "@user";
    private static final String USER_PASSWORD = "fakepassword";
    private static final String TEST_POST = "Talk about a recluse. He only comes out once a year, and" +
            "he never catches any flak for it! Probably lives up there to avoid the taxes.";
    private static final int PAGE_SIZE = 10;

    private TestMainView mainViewMock;
    private MainPresenter mainPresenterSpy;
    private ServerFacade serverFacade;
    private User expectedUser;
    private CountDownLatch countDownLatch;
    private Cache cacheMock;

    @BeforeEach
    public void setup() {
        serverFacade = new ServerFacade();
        cacheMock = Mockito.mock(Cache.class);
        mainViewMock = new TestMainView();
        mainPresenterSpy = Mockito.spy(new MainPresenter(mainViewMock));
        expectedUser = new User("Test", "User", "@user",
                "https://tweeterm4340.s3.us-west-2.amazonaws.com/%40user");
        Cache.setInstance(cacheMock);

        Mockito.when(cacheMock.getCurrUser()).thenReturn(expectedUser);

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    public void postStatusIntegrationTest_Success() throws InterruptedException, IOException, TweeterRemoteException {
        LoginResponse loginResponse = serverFacade.login(new LoginRequest(USER_ALIAS, USER_PASSWORD), LOGIN_URL);
        Assertions.assertNotNull(loginResponse);
        Assertions.assertNotNull(loginResponse.getAuthToken());
        Assertions.assertNotNull(loginResponse.getUser());
        Mockito.when(cacheMock.getCurrUserAuthToken()).thenReturn(loginResponse.getAuthToken());

        mainPresenterSpy.postStatus(TEST_POST);
        awaitCountDownLatch();

        Assertions.assertTrue(mainViewMock.isMessageSent());

        GetStoryRequest getStoryRequest = new GetStoryRequest(loginResponse.getAuthToken(),
                loginResponse.getUser().getAlias(), PAGE_SIZE, null);
        GetStoryResponse getStoryResponse = serverFacade.getStory(getStoryRequest, GET_STORY_URL);

        List<Status> storyPage = getStoryResponse.getStoryPage();
        while (getStoryResponse.getHasMorePages()) {
            getStoryRequest.setLastStatus(storyPage.get(storyPage.size() - 1));
            getStoryResponse = serverFacade.getStory(getStoryRequest, GET_STORY_URL);
            storyPage = getStoryResponse.getStoryPage();
        }

        Status actualStatus = storyPage.get(storyPage.size() - 1);
        Assertions.assertNotNull(actualStatus);
        Assertions.assertEquals(TEST_POST, actualStatus.getPost());
        Assertions.assertEquals(expectedUser, actualStatus.getUser());
    }

    private class TestMainView implements MainPresenter.MainView {
        private boolean messageSent = false;

        @Override
        public void isFollower(boolean isFollower) {}

        @Override
        public void updateSelectedUserFollowingAndFollowers() {}

        @Override
        public void updateFollowButton() {}

        @Override
        public void setFollowButtonEnabled(boolean value) {}

        @Override
        public void logoutUser() {}

        @Override
        public void cancelPostToast() {}

        @Override
        public void displayFollowerCount(int count) {}

        @Override
        public void displayFollowingCount(int count) {}

        @Override
        public void displayMessage(String message) {
            messageSent = true;
            countDownLatch.countDown();
        }

        public void setMessageSent(boolean messageSent) {
            this.messageSent = messageSent;
        }

        public boolean isMessageSent() {
            return messageSent;
        }
    }
}
