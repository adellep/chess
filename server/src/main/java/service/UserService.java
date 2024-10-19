package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) {
        try {
             UserData foundUser = Service.userDao.getUser(user.username());

             return null;

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
//    public AuthData login(UserData user) {}
//    public void logout(AuthData auth) {}
}
