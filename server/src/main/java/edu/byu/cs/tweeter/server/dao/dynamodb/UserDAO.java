package edu.byu.cs.tweeter.server.dao.dynamodb;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.server.dto.UserDTO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class UserDAO extends DAOUtils implements IUserDAO {
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_PARTITION_KEY = "alias";
    private static final int BATCH_SIZE = 25;
    private DynamoDbTable<UserDTO> userTable;

    private DynamoDbTable<UserDTO> getUserTable() {
        if (userTable == null) {
            userTable = getEnhancedClient().table(USER_TABLE_NAME, TableSchema.fromBean(UserDTO.class));
        }

        return userTable;
    }

    @Override
    public User register(RegisterRequest request) {
        String imageLocation = uploadImageToS3(request.getImageUrl(), request.getUsername());

        String salt = getSalt();
        String securePassword = getSecurePassword(request.getPassword(), salt);

        UserDTO user = new UserDTO();
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
        UserDTO user = getUserTable().getItem(key);

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
    public void decrementFollowersCount(String followeeAlias) {
        UserDTO user = getUserBean(followeeAlias);
        int followersCount = user.getFollowersCount();

        if (followersCount != 0) {
            followersCount--;
            user.setFollowersCount(followersCount);
            getUserTable().updateItem(user);
        }
    }

    @Override
    public void incrementFollowersCount(String followeeAlias) {
        UserDTO user = getUserBean(followeeAlias);
        int followersCount = user.getFollowersCount();

        followersCount++;
        user.setFollowersCount(followersCount);
        getUserTable().updateItem(user);
    }

    @Override
    public void decrementFollowingCount(String followerAlias) {
        UserDTO user = getUserBean(followerAlias);
        int followingCount = user.getFollowingCount();

        if (followingCount != 0) {
            followingCount--;
            user.setFollowingCount(followingCount);
            getUserTable().updateItem(user);
        }
    }

    @Override
    public void incrementFollowingCount(String followerAlias) {
        UserDTO user = getUserBean(followerAlias);
        int followingCount = user.getFollowingCount();

        followingCount++;
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

    @Override
    public void addUserBatch(List<UserDTO> users) {
        List<UserDTO> batch = new ArrayList<>();
        for (UserDTO user : users) {
            batch.add(user);

            if (batch.size() > 25) {
                writeBatchOfUserDTOs(batch);
                batch = new ArrayList<>();
            }
        }

        if (batch.size() > 0) {
            writeBatchOfUserDTOs(batch);
        }
    }

    private void writeBatchOfUserDTOs(List<UserDTO> batch) {
        if(batch.size() > 25) {
            throw new RuntimeException("Too many users to write");
        }

        WriteBatch.Builder<UserDTO> writeBuilder = WriteBatch.builder(UserDTO.class).mappedTableResource(getUserTable());
        for (UserDTO user : batch) {
            writeBuilder.addPutItem(builder -> builder.item(user));
        }

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = getEnhancedClient().batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(getUserTable()).size() > 0) {
                writeBatchOfUserDTOs(result.unprocessedPutItemsForTable(getUserTable()));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
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

    private UserDTO getUserBean(String alias) {
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

        URL url = s3.getUrl("tweeterm4340", userAlias);

        return url.toString();
    }
}
