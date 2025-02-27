package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    final private Map<Integer, GameData> games = new HashMap<>();
    int newGameID = 1;

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        newGameID = 1;
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        gameData = new GameData(newGameID++, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        games.put(gameData.gameID(), gameData);

        return gameData;
    }
}
