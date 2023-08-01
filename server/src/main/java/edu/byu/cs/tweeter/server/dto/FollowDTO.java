package edu.byu.cs.tweeter.server.dto;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FollowDTO {
    private static final String FOLLOWS_INDEX_NAME = "follow_index";
    private String follow_handle;
    private String follow_name;
    private String followee_handle;
    private String followee_name;
    private String follow_image;
    private String followee_image;


    public String getFollow_image() {
        return follow_image;
    }

    public void setFollow_image(String follow_image) {
        this.follow_image = follow_image;
    }

    public String getFollowee_image() {
        return followee_image;
    }

    public void setFollowee_image(String followee_image) {
        this.followee_image = followee_image;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FOLLOWS_INDEX_NAME)
    public String getFollow_handle() {
        return follow_handle;
    }

    public void setFollow_handle(String follow_handle) {
        this.follow_handle = follow_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = FOLLOWS_INDEX_NAME)
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }

    public String getFollow_name() {
        return follow_name;
    }

    public void setFollow_name(String follower_name) {
        this.follow_name = follower_name;
    }

    public String getFollowee_name() {
        return followee_name;
    }

    public void setFollowee_name(String followee_name) {
        this.followee_name = followee_name;
    }

    private String [] parseName(String fullName) {
        int spaceIndex = fullName.indexOf(" ");
        String firstName = fullName.substring(0, spaceIndex);
        String lastName = fullName.substring(spaceIndex + 1);

        return new String [] {firstName, lastName};
    }

    public User convertFollowerToUser() {
        String [] name = parseName(this.getFollowee_name());
        return new User(name[0], name[1], this.getFollowee_handle(), this.getFollowee_image());
    }

    public User convertFolloweeToUser() {
        String [] name = parseName(this.getFollow_name());
        return new User(name[0], name[1], this.getFollow_handle(), this.getFollow_image());
    }

    @Override
    public String toString() {
        return "Follow{" +
                "follow_handle=" + follow_handle +
                "follow_name=" + follow_name +
                "followee_handle=" + followee_handle +
                "followee_name=" + followee_name;
    }
}
