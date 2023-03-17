package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

public class GetFollowListTask extends PagedUserTask {

    public GetFollowListTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws Exception {
        FollowingRequest followingRequest = new FollowingRequest(authToken.getToken(), targetUser.getAlias(), limit, lastItem.getAlias());

        try {
            FollowingResponse followingResponse = getServerFacade().getFollowees(followingRequest, "getfollowing");
            return new Pair<>(followingResponse.getFollowees(), followingResponse.getHasMorePages());
        }
        catch (TweeterRemoteException | IOException e) {
            System.out.println(e.getMessage());
            throw new Exception("Exception caught while getting following");
        }
    }
}
