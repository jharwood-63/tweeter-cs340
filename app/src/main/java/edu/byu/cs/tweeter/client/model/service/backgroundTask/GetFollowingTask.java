package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;

public class GetFollowingTask extends GetFollowListTask<GetFollowingRequest> {
    private GetFollowingResponse response;
    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected Boolean getHasMorePages() {
        return response.getHasMorePages();
    }

    @Override
    protected List<User> getList() {
        return response.getFollowees();
    }

    @Override
    protected void setFollowResponse(GetFollowingRequest request) throws IOException, TweeterRemoteException {
        response = getServerFacade().getFollowing(request, "getfollowing");
    }

    @Override
    protected GetFollowingRequest getRequest(AuthToken authToken, String userAlias, int limit, String lastAlias) {
        return new GetFollowingRequest(authToken, userAlias, limit, lastAlias);
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
