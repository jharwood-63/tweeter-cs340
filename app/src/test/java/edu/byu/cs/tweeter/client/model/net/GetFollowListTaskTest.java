package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowListTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowListTaskTest {
    @Test
    public void getFollowingTest() {
        AuthToken authToken = new AuthToken("12345", "3/15/23");
        User targetUser = new User("Jackson", "Harwood", "@alias", "imageurl");
        User lastItem = new User("Alexis", "Harwood", "@another", "anotherurl");
        GetFollowListTask followListTask = new GetFollowListTask(authToken, targetUser, 5, lastItem, null);
        followListTask.run();
    }
}
