package dataaccess;

import model.UserData;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UserDAOMySql implements UserDAO {
    public UserDAOMySql() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "DELETE FROM user";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, userData.username(), userData.password(), userData.email());

        return userData;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT * FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error retrieving user: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    private void configureDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                    CREATE TABLE IF NOT EXISTS  user(
                    username varchar(255) NOT NULL UNIQUE,
                    password varchar(255) NOT NULL,
                    email varchar(255) NOT NULL UNIQUE,
                    PRIMARY KEY (username)
                    )
                    """;
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Can't confirgure user db: " + ex.getMessage());
        }
    }
}
