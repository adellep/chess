package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;

public interface AuthDAO {
    void clear() throws DataAccessException;
    AuthData createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
}
