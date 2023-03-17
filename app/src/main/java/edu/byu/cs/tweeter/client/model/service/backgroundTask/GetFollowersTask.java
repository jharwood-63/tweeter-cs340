package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;

public class GetFollowersTask extends GetFollowListTask<GetFollowersRequest, GetFollowersResponse> {
    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected Boolean getHasMorePages(GetFollowersResponse response) {
        return response.getHasMorePages();
    }

    @Override
    protected List<User> getList(GetFollowersResponse response) {
        return response.getFollowers();
    }

    @Override
    protected GetFollowersResponse getFollowList(GetFollowersRequest request) {
        //FIXME: IMPLEMENT THIS FUNCTION -> call serverFacade()
        return null;
    }

    @Override
    protected GetFollowersRequest getRequest(String token, String userAlias, int limit, String lastAlias) {
        return new GetFollowersRequest(token, userAlias, limit, lastAlias);
    }
}
