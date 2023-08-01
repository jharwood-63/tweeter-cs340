package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.queue.PostStatusQueue;

public class Service {
    private IAuthTokenDAO authTokenDAO;

    protected IAuthTokenDAO getAuthTokenDAO(DAOFactory factory) {
        if (authTokenDAO == null) {
            authTokenDAO = factory.getAuthTokenDAO();
        }

        return authTokenDAO;
    }

    protected String serializeQueue(Object requestInfo) {
        return (new Gson().toJson(requestInfo));
    }

    protected <T> T deserializeQueue(String response, Class<T> returnType) {
        return (new Gson().fromJson(response, returnType));
    }

    protected <T> void sendMessageToQueue(T queue, String queueUrl) {
        String messageBody = serializeQueue(queue);

        SendMessageRequest messageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult messageResult = sqs.sendMessage(messageRequest);

        System.out.println("Message ID: " + messageResult.getMessageId());
    }
}
