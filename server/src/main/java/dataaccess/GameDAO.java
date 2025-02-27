package dataaccess;

import model.GameData;

import javax.xml.crypto.Data;

public interface GameDAO {
    void clear() throws DataAccessException;
    //createGame
    GameData createGame(GameData gameData) throws DataAccessException;
    //getGame
    //listGames
    //updateGame
}
