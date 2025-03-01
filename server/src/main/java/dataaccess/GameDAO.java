package dataaccess;

import model.GameData;
import java.util.List;

import javax.xml.crypto.Data;

public interface GameDAO {
    void clear() throws DataAccessException;
    //createGame
    GameData createGame(GameData gameData) throws DataAccessException;
    //getGame
    List<GameData> getGames() throws DataAccessException;
    //listGames
    //updateGame
}
