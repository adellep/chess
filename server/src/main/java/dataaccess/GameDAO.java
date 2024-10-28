package dataaccess;

import model.GameData;
import java.util.List;

public interface GameDAO {
    void clear() throws DataAccessException;
    GameData createGame(GameData gameData) throws DataAccessException;
    List<GameData> getGames() throws DataAccessException;

}
