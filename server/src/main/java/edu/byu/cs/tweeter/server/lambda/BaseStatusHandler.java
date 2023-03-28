package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IStatusDAO;
import edu.byu.cs.tweeter.server.service.StatusService;

public class BaseStatusHandler extends Handler<StatusService, IStatusDAO> {
    @Override
    protected IStatusDAO createDAO(DAOFactory factory) {
        return factory.getStatusDAO();
    }

    @Override
    protected StatusService createService(IStatusDAO dao) {
        return new StatusService(dao);
    }
}
