package dataaccess;

import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UserDAOMySql implements UserDAO {
    public UserDAOMySql() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void addUser(UserData userData) {

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
                    CREATE TABLE IF NOT EXISTS  user(
                    id int NOT NULL AUTO_INCREMENT,
                    username varchar(255) NOT NULL UNIQUE,
                    password varchar(255) NOT NULL,
                    email varchar(255) NOT NULL UNIQUE,
                    PRIMARY KEY (id)
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
