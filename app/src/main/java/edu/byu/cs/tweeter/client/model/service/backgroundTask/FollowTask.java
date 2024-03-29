package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    /**
     * The user that is being followed.
     */
    private final User followee;
    private final User follower;

    public FollowTask(AuthToken authToken, User followee, User follower, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
        this.follower = follower;
    }

    @Override
    protected void runTask() {
        try {
            getServerFacade().follow(new FollowRequest(authToken, followee, follower), "follow");
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
