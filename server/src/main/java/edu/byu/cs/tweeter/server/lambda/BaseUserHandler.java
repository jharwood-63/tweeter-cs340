package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.server.service.UserService;

public class BaseUserHandler extends Handler<UserService> {
    @Override
    protected UserService createService() {
        return new UserService();
    }
}
