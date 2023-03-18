package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.util.Pair;

public class GetStoryTask extends PagedStatusTask {
    private GetStoryResponse response;
    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() throws IOException, TweeterRemoteException {
        GetStoryRequest request = new GetStoryRequest(authToken.getToken(), targetUser.getAlias(), limit, lastItem);
        response = getServerFacade().getStory(request, "getstory");

        if (response.isSuccess()) {
            return new Pair<>(response.getStoryPage(), response.getHasMorePages());
        }
        else {
            return null;
        }
    }

    @Override
    protected String getFailedMessage() {
        return response.getMessage();
    }

    @Override
    protected boolean isSuccess() {
        return response.isSuccess();
    }
}
