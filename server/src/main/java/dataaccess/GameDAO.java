package dataaccess;

import model.GameData;

public interface GameDAO {
    void clear() throws DataAccessException;
    GameData createGame(GameData gameData) throws DataAccessException;
}
