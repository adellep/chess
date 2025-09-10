package dataaccess;

import java.sql.SQLException;

public class MySqlDataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS user (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
        )
        """,
            """
        CREATE TABLE IF NOT EXISTS game (
            gameID INT NOT NULL AUTO_INCREMENT,
            gameName VARCHAR(255) NOT NULL,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            game JSON NOT NULL,
            PRIMARY KEY (gameID),
            FOREIGN KEY (whiteUsername) REFERENCES user(username) ON DELETE SET NULL,
            FOREIGN KEY (blackUsername) REFERENCES user(username) ON DELETE SET NULL
        )
        """,
            """
        CREATE TABLE IF NOT EXISTS auth (
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken),
            FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE
        )
        """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to confirgure data: " + ex.getMessage());
        }
    }
}
