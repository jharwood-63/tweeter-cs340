package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.util.Pair;

public abstract class GetFollowListTask<T, U> extends PagedUserTask {

    public GetFollowListTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws Exception {
        T request = getRequest(authToken.getToken(), targetUser.getAlias(), limit, lastItem.getAlias());

        try {
            U response = getFollowList(request);
            return new Pair<>(getList(response), getHasMorePages(response));
        }
        catch (TweeterRemoteException | IOException e) {
            System.out.println(e.getMessage());
            throw new Exception("Exception caught while getting following");
        }
    }

    protected abstract Boolean getHasMorePages(U response);

    protected abstract List<User> getList(U response);

    protected abstract U getFollowList(T request) throws IOException, TweeterRemoteException;

    protected abstract T getRequest(String token, String userAlias, int limit, String lastAlias);
}
