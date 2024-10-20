package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameDAOMemory implements  GameDAO {

    final private Map<String, GameData> games = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
