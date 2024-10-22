package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;
    AuthData createAuth(AuthData authData) throws DataAccessException;
}
