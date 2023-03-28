package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.server.service.UserService;

public class BaseUserHandler extends Handler<UserService, IUserDAO> {
    @Override
    protected IUserDAO createDAO(DAOFactory factory) {
        return factory.getUserDAO();
    }

    @Override
    protected UserService createService(IUserDAO dao) {
        return new UserService(dao);
    }
}
