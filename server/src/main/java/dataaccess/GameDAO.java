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
    GameData getGame(int gameID) throws DataAccessException;
    //listGames
    //updateGame
    boolean freePlayerColor(int gameID, String playerColor) throws  DataAccessException;
    void addPlayer(int gameID, String username, String playerColor) throws DataAccessException;
}
