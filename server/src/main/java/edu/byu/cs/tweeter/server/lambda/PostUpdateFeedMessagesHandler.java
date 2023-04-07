package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public class PostUpdateFeedMessagesHandler extends BaseFollowHandler implements RequestHandler<SQSEvent, Void> {
    //uses followService and followDAO
    //pull from postStatusQueue
    //push to UpdateFeedQueue
    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        String messageBody = null;
        for (SQSEvent.SQSMessage msg : input.getRecords()) {
            messageBody = msg.getBody();
            System.out.println(messageBody);
        }

        if (messageBody != null) {
            getService().getFollowersToUpdateFeed(messageBody);
        }
        else {
            throw new RuntimeException("There is no message");
        }
        return null;
    }
}
