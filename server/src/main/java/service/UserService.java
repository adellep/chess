package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {

    public AuthData register(UserData user) {

        try {
             UserData foundUser = Service.userDao.getUser(user.username());

             //user already exists
             if (foundUser != null) {
                 return null;
             }

             Service.userDao.addUser(user);
             String authToken = "hardCodedAuthTok4884";
             return new AuthData(authToken, user.username());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
//    public AuthData login(UserData user) {}
//    public void logout(AuthData auth) {}
}
