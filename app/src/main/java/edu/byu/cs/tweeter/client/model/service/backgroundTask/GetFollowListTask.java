package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.util.Pair;

public abstract class GetFollowListTask<T> extends PagedUserTask {

    public GetFollowListTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws Exception {
        T request;
        if (lastItem != null) {
            request = getRequest(authToken, targetUser.getAlias(), limit, lastItem.getAlias());
        }
        else {
            request = getRequest(authToken, targetUser.getAlias(), limit, null);
        }

        setFollowResponse(request);
        return new Pair<>(getList(), getHasMorePages());
    }

    protected abstract Boolean getHasMorePages();

    protected abstract List<User> getList();

    protected abstract void setFollowResponse(T request) throws IOException, TweeterRemoteException;

    protected abstract T getRequest(AuthToken authToken, String userAlias, int limit, String lastAlias);
}
