package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;

public class MainPresenterUnitTest {
    private MainPresenter.MainView mockView;
    private StatusService mockStatusService;
    private Cache mockCache;
    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup() {
        // Create mocks
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
//        Mockito.doReturn(mockStatusService).when(mainPresenterSpy).getStatusService();
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }

    // 29:38 in video https://www.youtube.com/watch?v=bdu6FSBgm74

    @Test
    public void testPostStatus_postSuccessful() {

    }

    @Test
    public void testPostStatus_postFailedWithMessage() {

    }

    @Test
    public void testPostStatus_postFailedWithException() {

    }
}
