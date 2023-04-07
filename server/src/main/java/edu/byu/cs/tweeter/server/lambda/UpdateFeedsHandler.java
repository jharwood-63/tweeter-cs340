package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public class UpdateFeedsHandler extends BaseStatusHandler implements RequestHandler<SQSEvent, Void> {
    //pulls from updateFeedQueue
    //uses statusService and feedDAO
    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        String messageBody = null;
        for (SQSEvent.SQSMessage msg : input.getRecords()) {
            messageBody = msg.getBody();
            System.out.println(messageBody);
        }

        if (messageBody != null) {
            getService().updateFeed(messageBody);
        }
        else {
            throw new RuntimeException("There is no message");
        }
        return null;
    }
}
