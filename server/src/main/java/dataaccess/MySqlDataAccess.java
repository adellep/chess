package dataaccess;

import java.sql.SQLException;

public class MySqlDataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }
//user table: username, hashed password, email
//game table: gameID, white, black, gameName, ChessGame state
//auth table: authtoken, username

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
    //foreign key on delete set null - prevent deletion of reference user if related records in table
    //foreign key on delete cascade -if user is deleted, all rows in table automatically deleted
    //good practice?

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate(); //exUpdate for inserting, updating, and deleting records, need later
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to confirgure data: " + ex.getMessage());
        }
    }
}
