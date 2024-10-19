package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void addUser(UserData userData);
    UserData createUser(UserData userData) throws DataAccessException;
    UserData createAuth(AuthData authData) throws DataAccessException;
}
