package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.ResponseException;
import request.CreateGameRequest;
import result.CreateGameResult;

public class CreateGameService {

    final private AuthDAO authDAO;
    final private GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException {
        try {
            var authToken = authDAO.getAuth(request.authToken());

            if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null; 
    }
}
