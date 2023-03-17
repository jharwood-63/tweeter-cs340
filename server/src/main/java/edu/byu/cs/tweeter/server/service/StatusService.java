package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class StatusService {
    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getToken() == null || request.getToken().equals("")) {
            throw new RuntimeException("[Bad Request] Unauthenticated User");
        }
        else if (request.getStatus().getPost().equals("") || request.getStatus().getPost() == null) {
            throw new RuntimeException("[Bad Request] Request must have a post");
        }

        return new PostStatusResponse(true);
    }
}
