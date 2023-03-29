package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.util.Pair;

public class GetFeedTask extends PagedStatusTask {
    private GetFeedResponse response;

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() throws IOException, TweeterRemoteException {
        GetFeedRequest request = new GetFeedRequest(authToken, targetUser.getAlias(), limit, lastItem);
        response = getServerFacade().getFeed(request, "getfeed");
        return new Pair<>(response.getFeedPage(), response.getHasMorePages());
    }

    @Override
    protected boolean isSuccess() {
        return response.isSuccess();
    }

    @Override
    protected String getFailedMessage() {
        return response.getMessage();
    }
}
