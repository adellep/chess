package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;

public interface AuthDAO {
    void clear() throws DataAccessException;
    //createAuth
    AuthData createAuth(AuthData authData) throws DataAccessException;
    //getAuth
    AuthData getAuth(String authToken) throws DataAccessException;
    //deleteAuth
    void deleteAuth(String authToken) throws DataAccessException;
}
