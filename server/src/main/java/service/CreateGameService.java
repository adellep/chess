package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.ResponseException;
import model.GameData;

import java.util.UUID;

public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException {
        try {
            var authData = authDAO.getAuth(request.authToken());

            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            ChessGame chessGame = new ChessGame();
            //int gameID = generateGameID();
            GameData newGame = new GameData(1, authData.username(), null, chessGame);
            GameData saveGame = gameDAO.createGame(newGame);

            return new CreateGameResult(saveGame.gameID());

        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

//    public static int generateGameID() {
//        return UUID.randomUUID().hashCode();
//    }
}