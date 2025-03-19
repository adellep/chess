package dataaccess;

import model.AuthData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthDAOMySql implements AuthDAO {
    public AuthDAOMySql() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private void configureDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                    CREATE TABLE IF NOT EXISTS  auth(
                    authToken varchar(256) NOT NULL,
                    username int NOT NULL,
                    PRIMARY KEY (authToken),
                    FOREIGN KEY (username) REFERENCES user(id) ON DELETE CASCADE
                    )
                    """;
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Can't configure user db: " + ex.getMessage());
        }
    }
}
