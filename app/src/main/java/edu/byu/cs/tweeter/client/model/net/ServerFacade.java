package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://pn02zd4r40.execute-api.us-west-2.amazonaws.com/dev";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public GetFollowingResponse getFollowing(GetFollowingRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetFollowingResponse.class);
    }

    public UnfollowResponse unfollow(UnfollowRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, UnfollowResponse.class);
    }

    public FollowResponse follow(FollowRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowResponse.class);
    }

    public GetCountResponse getCount(GetCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetCountResponse.class);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, IsFollowerResponse.class);
    }

    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);
    }

    public GetUserResponse getUser(GetUserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);
    }

    public GetFeedResponse getFeed(GetFeedRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetFeedResponse.class);
    }

    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);
    }

    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);
    }

    public GetFollowersResponse getFollowers(GetFollowersRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetFollowersResponse.class);
    }

    public GetStoryResponse getStory(GetStoryRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetStoryResponse.class);
    }
}
