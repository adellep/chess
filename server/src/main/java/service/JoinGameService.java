package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;
import results.JoinGameResult;


public class JoinGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

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
            if (gameData == null) {
                throw new ResponseException(400, "Error: bad request (game not found");
            }
            String playerColor = request.playerColor();
            if (playerColor == null) {
                throw new ResponseException(400, "Error: bad request (missing color");
            }

            if (playerColor.equalsIgnoreCase("OBSERVER")) {
                return new JoinGameResult(true, "Successfully observing game.");
            }

            if (!playerColor.equalsIgnoreCase("WHITE") && !playerColor.equalsIgnoreCase("BLACK")) {
                throw new ResponseException(400, "Error: bad request (invalid color");
            }

            String username = authData.username();
            String userInSpot = playerColor.equalsIgnoreCase("WHITE")
                    ? gameData.whiteUsername() : gameData.blackUsername();

            if (userInSpot != null) {
                throw new ResponseException(403, "Error: already taken");
            }

            else {
                gameDAO.addPlayer(request.gameID(), username, playerColor);
            }
            return new JoinGameResult(true, "Successfully joined game.");

        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
