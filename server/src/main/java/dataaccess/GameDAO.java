package dataaccess;

import model.GameData;
import java.util.List;

public interface GameDAO {
    void clear() throws DataAccessException;
    GameData createGame(GameData gameData) throws DataAccessException;
    List<GameData> getGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    boolean freePlayerColor(int gameID, String playerColor) throws DataAccessException;
    void addPlayer(int gameID, String username, String playerColor) throws DataAccessException;
}
