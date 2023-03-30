package edu.byu.cs.tweeter.server.dao.dynamodb;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class UserDAO extends DAOUtils implements IUserDAO {
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_PARTITION_KEY = "alias";

    @Override
    public void register(RegisterRequest request) {
        // put image in s3
        String imageLocation = uploadImageToS3(request.getImageUrl(), request.getUsername());
        // create userbean
        UserBean newUserBean = new UserBean();
        newUserBean.setAlias(request.getUsername());
        newUserBean.setName(request.getFirstName() + " " + request.getLastName());
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
    public void login(LoginRequest request) {

    }

    @Override
    public void getUser(GetUserRequest request) {

    }

    @Override
    public void logout(LogoutRequest request) {

    }

    @Override
    public boolean authenticateRequest(AuthToken authToken) {
        return false;
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
