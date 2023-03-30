package edu.byu.cs.tweeter.server.dao.dynamodb.bean;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedBean {
    private String receiverAlias;
    private long timestamp;
    private String post;
    private List<String> urls;
    private List<String> mentions;
    private String senderAlias;
    private String senderFirstName;
    private String senderLastName;
    private String senderImage;

    @DynamoDbPartitionKey
    public String getReceiverAlias() {
        return receiverAlias;
    }

    public void setReceiverAlias(String receiverAlias) {
        this.receiverAlias = receiverAlias;
    }

    @DynamoDbSortKey
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public String getSenderAlias() {
        return senderAlias;
    }

    public void setSenderAlias(String senderAlias) {
        this.senderAlias = senderAlias;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public Status convertFeedToStatus() {
        User senderUser = new User(this.getSenderFirstName(), this.getSenderLastName(), this.getSenderAlias(), this.getSenderImage());
        return new Status(this.getPost(), senderUser, String.valueOf(this.getTimestamp()), this.getUrls(), this.getMentions());
    }
}
