package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IFollowDAO;
import edu.byu.cs.tweeter.server.service.FollowService;

public class BaseFollowHandler extends Handler<FollowService, IFollowDAO> {
    @Override
    protected IFollowDAO createDAO(DAOFactory factory) {
        return factory.getFollowDAO();
    }

    @Override
    protected FollowService createService(IFollowDAO followDAO) {
        return new FollowService(followDAO);
    }
}
