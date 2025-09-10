package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAOMemory implements  AuthDAO {

    final private Map<String, AuthData> tokens = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        tokens.clear();
    }

    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        tokens.put(authData.authToken(), authData);

        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return tokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        tokens.remove(authToken);
    }
}
