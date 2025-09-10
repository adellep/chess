package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.executeUpdate;

public class UserDAOMySql implements UserDAO {

    public UserDAOMySql() throws DataAccessException {
        configureDatabase();
    }

@Override
public void addUser(UserData userData) throws DataAccessException {
    String keepPassword = userData.password();

    if (keepPassword != null && !keepPassword.startsWith("$2a$")) {
        keepPassword = BCrypt.hashpw(keepPassword, BCrypt.gensalt());
    }

    var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
    DatabaseManager.executeUpdate(statement, userData.username(), keepPassword, userData.email());
}

    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT * FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error getting user: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        addUser(userData);
        return getUser(userData.username());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "DELETE FROM user";
        DatabaseManager.executeUpdate(statement);
    }

    private void configureDatabase() throws DataAccessException {
        var createStatement = """
            CREATE TABLE IF NOT EXISTS user (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
        )
        """;
        DatabaseManager.executeUpdate(createStatement);
    }
}
