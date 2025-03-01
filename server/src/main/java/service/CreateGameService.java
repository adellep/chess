package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.ResponseException;
import model.GameData;
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

            ChessGame chessGame = new ChessGame();
            GameData newGame = new GameData(1, null, null, request.gameName(), chessGame);
            GameData saveGame = gameDAO.createGame(newGame);

            return new CreateGameResult(saveGame.gameID());

        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
