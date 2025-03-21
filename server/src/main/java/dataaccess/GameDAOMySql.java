package dataaccess;

import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class GameDAOMySql implements GameDAO {
    public GameDAOMySql() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "DELETE FROM game";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        DatabaseManager.executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());

        return gameData;
    }

    @Override
    public List<GameData> getGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public boolean freePlayerColor(int gameID, String playerColor) throws DataAccessException {
        return false;
    }

    @Override
    public void addPlayer(int gameID, String username, String playerColor) throws DataAccessException {

    }
//gameState TEXT NOT NULL? game vs gameState
    private void configureDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                    CREATE TABLE IF NOT EXISTS  game(
                    gameID int PRIMARY KEY AUTO_INCREMENT,
                    whiteUsername int DEFAULT NULL,
                    blackUsername int DEFAULT NULL,
                    gameName varchar(256) NOT NULL,
                    game TEXT NOT NULL
                  
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
