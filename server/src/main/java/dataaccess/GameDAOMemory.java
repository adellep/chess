package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDAOMemory implements  GameDAO {

    private final Map<Integer, GameData> games = new HashMap<>();
    int newGameID = 1;

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        newGameID = 1;
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        gameData = new GameData(newGameID++, gameData.gameName(), gameData.whiteUsername(), gameData.blackUsername(), gameData.game());
        games.put(gameData.gameID(), gameData);

        return gameData;
    }

    @Override
    public List<GameData> getGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public boolean freePlayerColor(int gameID, String playerColor) throws DataAccessException {
       if (playerColor == null) {
           return false;
       }

        GameData game = games.get(gameID);
        if (game == null) {
            return false;
        }

        return (playerColor.toLowerCase().equals("white") && game.whiteUsername() == null) ||
                (playerColor.toLowerCase().equals("black") && game.blackUsername() == null);
    }

    @Override
    public void  addPlayer(int gameID, String username, String playerColor) throws DataAccessException {
        GameData game = games.get(gameID);

        if (game == null) {
            throw new DataAccessException("No game found");
        }

        String whiteUsername = playerColor.toLowerCase().equals("white") ? username : game.whiteUsername();
        String blackUsername = playerColor.toLowerCase().equals("black") ? username : game.blackUsername();

        GameData updateGame = new GameData(game.gameID(), game.gameName(), whiteUsername, blackUsername, game.game());
        games.put(gameID, updateGame);
    }

}
