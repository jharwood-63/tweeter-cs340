package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IStoryDAO;
import edu.byu.cs.tweeter.server.service.StatusService;

public class BaseStatusHandler extends Handler<StatusService> {
    @Override
    protected StatusService createService() {
        return new StatusService();
    }
}
