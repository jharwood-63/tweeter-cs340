package edu.byu.cs.tweeter.server.dao.dynamodb;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.FollowBean;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class UserDAO extends DAOUtils implements IUserDAO {
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_PARTITION_KEY = "alias";
    private DynamoDbTable<UserBean> userTable;

    private DynamoDbTable<UserBean> getUserTable() {
        if (userTable == null) {
            userTable = getEnhancedClient().table(USER_TABLE_NAME, TableSchema.fromBean(UserBean.class));
        }

        return userTable;
    }

    @Override
    public void register(RegisterRequest request) {
        // put image in s3
        String imageLocation = uploadImageToS3(request.getImageUrl(), request.getUsername());
        // create userbean
        UserBean newUserBean = new UserBean();
        newUserBean.setAlias(request.getUsername());
//        newUserBean.setName(request.getFirstName() + " " + request.getLastName());
        //FIXME: FIGURE OUT HOW TO HASH THIS FIRST
//        userBean.setPassword(request.getPassword());
        newUserBean.setImageLocation(imageLocation);
        // add user to db
        DynamoDbTable<UserBean> table = getEnhancedClient().table(USER_TABLE_NAME, TableSchema.fromBean(UserBean.class));
        Key key = Key.builder().partitionValue(USER_PARTITION_KEY).build();

        table.putItem(newUserBean);
        // create authToken
        // add authToken to db
    }

    @Override
    public void verifyCredentials(LoginRequest request) {

    }

    @Override
    public User getUser(GetUserRequest request) {
        Key key = Key.builder().partitionValue(request.getAlias()).build();
        return getUserTable().getItem(key).convertUserBeanToUser();
    }

    @Override
    public void updateFollowersCount(String followeeAlias, int value) {
        UserBean user = getUserBean(followeeAlias);
        int followersCount = user.getFollowersCount();
        followersCount += value;
        user.setFollowersCount(followersCount);
        getUserTable().updateItem(user);
    }

    @Override
    public void updateFollowingCount(String followerAlias, int value) {
        UserBean user = getUserBean(followerAlias);
        int followingCount = user.getFollowingCount();
        followingCount += value;
        user.setFollowingCount(followingCount);
        getUserTable().updateItem(user);
    }

    @Override
    public int getFollowingCount(String userAlias) {
        return getUserBean(userAlias).getFollowingCount();
    }

    @Override
    public int getFollowersCount(String userAlias) {
        return getUserBean(userAlias).getFollowersCount();
    }

    private UserBean getUserBean(String alias) {
        Key key = Key.builder().partitionValue(alias).build();
        return getUserTable().getItem(key);
    }

    public String uploadImageToS3(String imageUrl, String userAlias) {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();

        byte[] byteArray = Base64.getDecoder().decode(imageUrl);

        ObjectMetadata data = new ObjectMetadata();

        data.setContentLength(byteArray.length);

        data.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest("tweeterm4340", userAlias, new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(request);

        return "https://tweeter4340.s3.us-west-2.amazonaws.com/userImages" + userAlias;
    }
}
