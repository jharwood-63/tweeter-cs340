package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;

public class GetFollowersTask extends GetFollowListTask<GetFollowersRequest> {
    private GetFollowersResponse response;

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected GetFollowersRequest getRequest(String token, String userAlias, int limit, String lastAlias) {
        return new GetFollowersRequest(token, userAlias, limit, lastAlias);
    }

    @Override
    protected void setFollowResponse(GetFollowersRequest request) throws IOException, TweeterRemoteException {
        response = getServerFacade().getFollowers(request, "getfollowers");
    }

    @Override
    protected String getFailedMessage() {
        return response.getMessage();
    }

    @Override
    protected boolean isSuccess() {
        return response.isSuccess();
    }

    @Override
    protected Boolean getHasMorePages() {
        return response.getHasMorePages();
    }

    @Override
    protected List<User> getList() {
        return response.getFollowers();
    }
}
