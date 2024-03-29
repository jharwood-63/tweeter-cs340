package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {

    /**
     * The user that is being followed.
     */
    private final User followee;
    private final User currUser;

    public UnfollowTask(AuthToken authToken, User followee, User currUser, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
        this.currUser = currUser;
    }

    @Override
    protected void runTask() {
        try {
            UnfollowResponse response = getServerFacade().unfollow(new UnfollowRequest(authToken, followee, currUser), "unfollow");
            // We could do this from the presenter, without a task and handler, but we will
            // eventually access the database from here when we aren't using dummy data.

            // Call sendSuccessMessage if successful
            sendSuccessMessage();
            // or call sendFailedMessage if not successful
            // sendFailedMessage()
        }
        catch (Exception e) {
            sendExceptionMessage(e);
        }
    }


}
