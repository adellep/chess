package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import requests.*;

public class DataAccessTest {

    @Test
    public void clearTest() {
        var userDao = new UserDAOMemory();
        var gameDAO = new GameDAOMemory();
        var authDao = new AuthDAOMemory();
        var clearService = new ClearService(userDao, gameDAO, authDao);
        var result = clearService.clear();
        Assertions.assertNull(result.message());
    }

    @Test
    public void registerUserSuccess() throws DataAccessException, ResponseException {
        var userDao = new UserDAOMemory();
        var authDao = new AuthDAOMemory();
        var registerService = new RegisterService(userDao, authDao);
        var req = new RegisterRequest("a", "p", "a@a.com");
        var res = registerService.register(req);

        Assertions.assertNotNull(res);
        Assertions.assertEquals("a", res.username());
        Assertions.assertNotNull(res.authToken());
        Assertions.assertNotNull(userDao.getUser("a"));
    }

    @Test
    public void registerUserAlreadyExists() throws ResponseException {
        var userDao = new UserDAOMemory();
        var authDao = new AuthDAOMemory();
        var registerService = new RegisterService(userDao, authDao);
        var req1 = new RegisterRequest("a", "p", "a@a.com");
        var res1 = registerService.register(req1);

        var req2 = new RegisterRequest("a", "pp", "aa@aa.com");
        //var res2 = registerService.register(req2);

        Assertions.assertThrows(ResponseException.class, () -> registerService.register(req2));
    }

    @Test
    public void loginUserSuccess() throws ResponseException {
        var userDAO = new UserDAOMemory();
        var authDAO = new AuthDAOMemory();
        var loginService = new LoginService(userDAO, authDAO);

        var currentUser = new UserData("a", "p", "email");
        userDAO.addUser(currentUser);

        var req = new LoginRequest("a", "p");
        var res = loginService.login(req);

        Assertions.assertNotNull(res);
        Assertions.assertEquals("a", res.username());
        Assertions.assertNotNull(res.authToken());
        Assertions.assertFalse(res.authToken().isEmpty());
    }

    @Test
    public void loginUserWrongPassword() {
        var userDAO = new UserDAOMemory();
        var authDAO = new AuthDAOMemory();
        var loginService = new LoginService(userDAO, authDAO);

        var currentUser = new UserData("a", "p", "email");
        userDAO.addUser(currentUser);

        var wrongPassword = new LoginRequest("a", "wrongPass");

        Assertions.assertThrows(ResponseException.class, () -> loginService.login(wrongPassword));
    }

    @Test
    public void logoutUserSuccess() throws ResponseException, DataAccessException {
        var authDAO = new AuthDAOMemory();
        var logoutService = new LogoutService(authDAO);

        var authToken = "1234";
        authDAO.createAuth(new AuthData(authToken, "a"));

        var logoutRequest = new LogoutRequest(authToken);
        var logoutResult = logoutService.logout(logoutRequest);

        Assertions.assertNotNull(logoutResult);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }

    @Test
    public void logoutWrongAuthToken() {
        var authDAO = new AuthDAOMemory();
        var logoutService = new LogoutService(authDAO);

        var wrongAuthToken = "wrongToken";
        var logoutRequest = new LogoutRequest(wrongAuthToken);

        Assertions.assertThrows(ResponseException.class, () -> logoutService.logout(logoutRequest));

    }

    @Test
    public void createGameSuccess() throws ResponseException, DataAccessException {
        var authDAO = new AuthDAOMemory();
        var gameDAO = new GameDAOMemory();
        var createGameService = new CreateGameService(authDAO, gameDAO);
        var authData = new AuthData("token1", "username1");
        authDAO.createAuth(authData);

        var createGameReq = new CreateGameRequest("token1", "Game1");
        var createGameRes = createGameService.createGame(createGameReq);

        Assertions.assertNotNull(createGameRes);
    }

    @Test
    public void createGameWrongAuthToken() {
        var authDAO = new AuthDAOMemory();
        var gameDAO = new GameDAOMemory();
        var createGameService = new CreateGameService(authDAO, gameDAO);

        var wrongAuthToken = new CreateGameRequest("wrongToken", "Game1");

        Assertions.assertThrows(ResponseException.class, () -> createGameService.createGame(wrongAuthToken));

    }

    @Test
    public void listGamesSuccess() throws ResponseException, DataAccessException {
        var authDAO = new AuthDAOMemory();
        var gameDAO = new GameDAOMemory();
        var listGamesService = new ListGamesService(authDAO, gameDAO);
        var authData = new AuthData("token1", "username1");
        authDAO.createAuth(authData);

        var game1 = new GameData(1, "game1", "username1", null, null);
        var game2 = new GameData(2, "game2", null, "username2", null);
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);

        var listGamesResult = listGamesService.listGames("token1");

        Assertions.assertNotNull(listGamesResult);
        Assertions.assertEquals(2, listGamesResult.games().size());
    }

    @Test
    public void listGamesWrongAuthToken() {
        var authDAO = new AuthDAOMemory();
        var gameDAO = new GameDAOMemory();
        var listGamesService = new ListGamesService(authDAO, gameDAO);

        Assertions.assertThrows(ResponseException.class, () -> listGamesService.listGames("wrongAuthToken"));
    }

    @Test
    public void joinGameSuccess() throws ResponseException, DataAccessException {
        var authDAO = new AuthDAOMemory();
        var gameDAO = new GameDAOMemory();
        var joinGameService = new JoinGameService(authDAO, gameDAO);
        var authData = new AuthData("token1", "username1");
        authDAO.createAuth(authData);

        var game = new GameData(1, "game1", null, "username2", null);
        gameDAO.createGame(game);

        var joinRequest = new JoinGameRequest("token1", "white", 1);
        var joinResult = joinGameService.joinGame(joinRequest);

        Assertions.assertNotNull(joinResult);
    }

    @Test
    public void joinGameWrongAuthToken() throws DataAccessException {
        var authDAO = new AuthDAOMemory();
        var gameDAO = new GameDAOMemory();
        var joinGameService = new JoinGameService(authDAO, gameDAO);
        var game = new GameData(1, "game1", "username1", null, null);
        gameDAO.createGame(game);

        var joinRequest = new JoinGameRequest("wrongToken", "white", 1);

        Assertions.assertThrows(ResponseException.class, () -> joinGameService.joinGame(joinRequest));

    }
}