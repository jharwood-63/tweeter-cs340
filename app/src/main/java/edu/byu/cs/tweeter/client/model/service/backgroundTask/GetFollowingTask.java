package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;

public class GetFollowingTask extends GetFollowListTask<GetFollowingRequest, GetFollowingResponse> {
    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected Boolean getHasMorePages(GetFollowingResponse response) {
        return response.getHasMorePages();
    }

    @Override
    protected List<User> getList(GetFollowingResponse response) {
        return response.getFollowees();
    }

    @Override
    protected GetFollowingResponse getFollowList(GetFollowingRequest request) throws IOException, TweeterRemoteException {
        return getServerFacade().getFollowing(request, "getfollowing");
    }

    @Override
    protected GetFollowingRequest getRequest(String token, String userAlias, int limit, String lastAlias) {
        return new GetFollowingRequest(token, userAlias, limit, lastAlias);
    }
}
