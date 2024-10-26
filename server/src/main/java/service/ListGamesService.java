package service;

import dataaccess.*;
import model.GameData;

import java.util.List;

public class ListGamesService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

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

        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }

    }
}
