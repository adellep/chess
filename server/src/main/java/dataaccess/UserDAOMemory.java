package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class UserDAOMemory implements UserDAO {

    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

@Override
public void addUser(UserData userData) {
    String keepPassword = userData.password();

    if (keepPassword != null && !keepPassword.startsWith("$2a$")) {
        keepPassword = BCrypt.hashpw(keepPassword, BCrypt.gensalt());
    }

    UserData userToStore = new UserData(userData.username(), keepPassword, userData.email());
    users.put(userToStore.username(), userToStore);
}

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        addUser(userData);
        return getUser(userData.username());
    }
}

