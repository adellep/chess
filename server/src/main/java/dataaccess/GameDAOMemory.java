package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDAOMemory implements  GameDAO {

    final private Map<Integer, GameData> games = new HashMap<>();
    int newGameID = 1;

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        newGameID = 1;
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        gameData = new GameData(newGameID++, gameData.whiteUsername(), gameData.blackUsername(), gameData.game());
        games.put(gameData.gameID(), gameData);

        return gameData;
    }

    @Override
    public List<GameData> getGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }

}
