package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.UserDAOMemory;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

public class UserService {

    private final UserDAO userDao; //?

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public AuthData register(UserData user) {

        try {
             UserData foundUser = userDao.getUser(user.username());

             //user already exists
             if (foundUser != null) {
                 return null;
             }

             userDao.addUser(user);
             String authToken = "hardCodedAuthTok4884";
             return new AuthData(authToken, user.username());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
//    public AuthData login(UserData user) {}
//    public void logout(AuthData auth) {}
}
