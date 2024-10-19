package service;

import dataaccess.UserDAOMemory;
import dataaccess.UserDAO;

public class Service {
    //store reference to daos that will be used

    static UserDAO userDao;

    static {
        userDao = new UserDAOMemory();
    }
    //**need another package for userdata in shared src java
//    public UserData registerUser(UserData newUser) {
//        return newUser;
//    }
}
