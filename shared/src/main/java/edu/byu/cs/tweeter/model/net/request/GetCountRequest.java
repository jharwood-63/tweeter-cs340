package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetCountRequest extends AuthenticatedRequest {
    private String userAlias;

    private GetCountRequest() {}

    public GetCountRequest(AuthToken authToken, String userAlias) {
        super(authToken);
        this.userAlias = userAlias;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
}
