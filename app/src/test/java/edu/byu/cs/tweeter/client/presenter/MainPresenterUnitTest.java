package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest {
    private MainPresenter.MainView mockView;
    private StatusService mockStatusService;
    private Cache mockCache;
    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }

    @Test
    public void testPostStatus_postSuccessful() {
        Answer<Void> successAnswer = new HandlerAnswer() {
            @Override
            public void handleResult(MainPresenter.PostObserver observer) {
                observer.handleSuccess();
            }
        };

        verifyResult(successAnswer, "Successfully Posted!", 1);
    }

    @Test
    public void testPostStatus_postFailedWithMessage() {
        Answer<Void> messageAnswer = new HandlerAnswer() {
            @Override
            public void handleResult(MainPresenter.PostObserver observer) {
                observer.handleFailure("The error message");
            }
        };

        verifyResult(messageAnswer, "Failed to post status: The error message", 0);
    }

    @Test
    public void testPostStatus_postFailedWithException() {
        Answer<Void> exceptionAnswer = new HandlerAnswer() {
            @Override
            public void handleResult(MainPresenter.PostObserver observer) {
                observer.handleException(new Exception("Exception message"));
            }
        };

        verifyResult(exceptionAnswer, "Failed to post status because of exception: Exception message", 0);
    }

    private void verifyResult(Answer<Void> answer, String message, int wantedNumberOfInvocations) {
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus("Test status to post");

        Mockito.verify(mockView).displayMessage("Posting status...");
        Mockito.verify(mockView, Mockito.times(wantedNumberOfInvocations)).cancelPostToast();
        Mockito.verify(mockView).displayMessage(message);
    }

    private abstract class HandlerAnswer implements Answer<Void> {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            Status status = invocation.getArgument(0, Status.class);
            MainPresenter.PostObserver observer = invocation.getArgument(1, MainPresenter.PostObserver.class);
            handleResult(observer);
            return null;
        }

        public abstract void handleResult(MainPresenter.PostObserver observer);
    }
}
