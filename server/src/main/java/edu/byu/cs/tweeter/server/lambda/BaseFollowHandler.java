package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.service.FollowService;

public class BaseFollowHandler extends Handler<FollowService> {
    @Override
    protected FollowService createService() {
        return new FollowService();
    }
}
