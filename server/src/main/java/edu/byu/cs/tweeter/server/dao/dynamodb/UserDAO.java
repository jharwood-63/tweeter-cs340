package edu.byu.cs.tweeter.server.dao.dynamodb;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
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
    public User register(RegisterRequest request) {
        String imageLocation = uploadImageToS3(request.getImageUrl(), request.getUsername());

        String salt = getSalt();
        String securePassword = getSecurePassword(request.getPassword(), salt);

        UserBean user = new UserBean();
        user.setAlias(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(securePassword);
        user.setSalt(salt);
        user.setImageLocation(imageLocation);
        user.setFollowersCount(0);
        user.setFollowingCount(0);
        getUserTable().putItem(user);

        return user.convertUserBeanToUser();
    }

    @Override
    public User login(LoginRequest request) {
        Key key = Key.builder().partitionValue(request.getUsername()).build();
        UserBean user = getUserTable().getItem(key);

        if (user != null) {
            String secureSuppliedPassword = getSecurePassword(request.getPassword(), user.getSalt());

            if (secureSuppliedPassword.equals(user.getPassword())) {
                return user.convertUserBeanToUser();
            }
        }

        return null;
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

    private String getSecurePassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("[Server Error] Unable to hash password!");
        }
    }

    private String getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);

            return Base64.getEncoder().encodeToString(salt);
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("[Server Error] Unable to get salt!");
        }
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
