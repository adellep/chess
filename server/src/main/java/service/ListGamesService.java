package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.ResponseException;
import model.GameData;
import org.eclipse.jetty.server.Response;
import result.ListGamesResult;

import java.util.List;

public class ListGamesService {
    //whiteUsername and blackUsername might be null

    final private AuthDAO authDAO;
    final private GameDAO gameDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) throws ResponseException, DataAccessException {
        try {
            var authData = authDAO.getAuth(authToken);

            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            List<GameData> games = gameDAO.getGames();
            return new ListGamesResult(games);
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
