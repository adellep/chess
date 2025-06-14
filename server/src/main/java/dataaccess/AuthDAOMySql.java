package dataaccess;

import model.AuthData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.executeUpdate;

public class AuthDAOMySql implements AuthDAO {

    public AuthDAOMySql() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.authToken(), authData.username());

        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString("authToken"), rs.getString("username"));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error retrieving auth token: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        DatabaseManager.executeUpdate(statement);
    }

    private void configureDatabase() throws DataAccessException {
        var createStatement = """
            CREATE TABLE IF NOT EXISTS auth (
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken),
            FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE
        )
        """;
        DatabaseManager.executeUpdate(createStatement);
    }

}
