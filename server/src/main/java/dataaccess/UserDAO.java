package dataaccess;

import model.UserData;

public interface UserDAO {
    //data access class - store and retrieve server data
    //create, read, update, delete
    //clear
    void clear() throws DataAccessException;
    //createUser
    UserData createUser(UserData userData) throws DataAccessException;
    //getuser
    UserData getUser(String username) throws  DataAccessException;
    void addUser(UserData userData);
}
