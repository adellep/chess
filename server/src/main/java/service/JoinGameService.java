package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.ResponseException;
import request.JoinGameRequest;
import result.JoinGameResult;

public class JoinGameService {

    final private AuthDAO authDAO;
    final private GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }



    public JoinGameResult joinGame(JoinGameRequest request) throws ResponseException {
        try {
            var authData = authDAO.getAuth(request.authToken());
            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            var gameData = gameDAO.getGame(request.gameID());
            if (gameData == null || request.playerColor() == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            if (!gameDAO.freePlayerColor(request.gameID(), request.playerColor())) {
                throw new ResponseException(403, "Error: already taken");
            }

            gameDAO.addPlayer(request.gameID(), authData.username(), request.playerColor());

            return new JoinGameResult(true, "added player");
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
