package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dataaccess.DatabaseManager.executeUpdate;

public class GameDAOMySql implements GameDAO {

    public GameDAOMySql() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO game (gameName, gameState) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, gameData.gameName(), "{}");

        var getLastID = "SELECT LAST_INSERT_ID()";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(getLastID);
             var rs = ps.executeQuery()) {
            if (rs.next()) {
                return new GameData(
                        rs.getInt(1),
                        gameData.gameName(),
                        null, null,
                        new ChessGame()
                );
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error creating game: " + ex.getMessage());
        }
        return null;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT * FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GameData(
                            rs.getInt("gameID"),
                            rs.getString("gameName"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            new ChessGame()
                    );
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error getting game: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<GameData> getGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        var statement = "SELECT * FROM game";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                games.add(new GameData(
                        rs.getInt("gameID"),
                        rs.getString("gameName"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        new ChessGame()
                ));
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error retrieving games: " + ex.getMessage());
        }
        return games;
    }

    @Override
    public boolean freePlayerColor(int gameID, String playerColor) throws DataAccessException {
        var column = playerColor.equalsIgnoreCase("WHITE") ? "whiteUsername" : "blackUsername";
        var statement = "SELECT " + column + " FROM game WHERE gameID=?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(column) == null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error checking free player color: " + ex.getMessage());
        }
        return false;
    }

    @Override
    public void addPlayer(int gameID, String username, String playerColor) throws DataAccessException {
        var column = playerColor.equalsIgnoreCase("WHITE") ? "whiteUsername" : "blackUsername";
        var statement = "UPDATE game SET " + column + "=? WHERE gameID=?";
        executeUpdate(statement, username, gameID);
    }

//    @Override
//    public void deleteGame(int gameID) throws DataAccessException {
//        var statement = "DELETE FROM game WHERE gameID=?";
//        DatabaseManager.executeUpdate(statement, gameID);
//    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        DatabaseManager.executeUpdate(statement);
    }

    private void configureDatabase() throws DataAccessException {
        var createStatement = """
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
        """;
        DatabaseManager.executeUpdate(createStatement);
    }

}
