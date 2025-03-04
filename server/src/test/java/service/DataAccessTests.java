package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.*;
import result.JoinGameResult;

import javax.xml.crypto.Data;

public class DataAccessTests {

    @Test
    public void clearTest() {
        var userDAO = new MemoryUserDAO();
        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();
        var clearService = new ClearService(userDAO, gameDAO, authDAO);
        var result = clearService.clear();

        Assertions.assertNull(result.message());
    }

    @Test
    public void registerSuccess() throws DataAccessException, ResponseException {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var registerService = new RegisterService(userDAO, authDAO);
        var request = new RegisterRequest("a", "p", "a@gmail.com");
        var response = registerService.register(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.authToken());
        Assertions.assertNotNull(userDAO.getUser("a"));
        Assertions.assertEquals("a", response.username());
    }

    @Test
    public void registerFail() throws DataAccessException, ResponseException { //user exists already
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var registerService = new RegisterService(userDAO, authDAO);
        var request1 = new RegisterRequest("a", "p", "a@gmail.com");
        var request2 = new RegisterRequest("a", "b", "b@gmail.com");
        var response = registerService.register(request1);

        Assertions.assertThrows(ResponseException.class, () -> registerService.register(request2));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var loginService = new LoginService(userDAO, authDAO);
        var currentUser = new UserData("a", "p", "a@gmail.com");
        userDAO.addUser(currentUser);

        var request = new LoginRequest("a", "p");
        var response = loginService.login(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.authToken());
        Assertions.assertEquals("a", response.username());
        Assertions.assertFalse(response.authToken().isEmpty());
    }

    @Test
    public void loginFail() { //wrong password
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var loginService = new LoginService(userDAO, authDAO);
        var currentUser = new UserData("a", "p", "a@gmail.com");
        userDAO.addUser(currentUser);

        var wrongPassword = new LoginRequest("a", "b");

        Assertions.assertThrows(ResponseException.class, () -> loginService.login(wrongPassword));
    }

    @Test
    public void logoutSuccess() throws ResponseException, DataAccessException {
        var authDAO = new MemoryAuthDAO();
        var logoutService = new LogoutService(authDAO);
        var authToken = "1111";
        authDAO.createAuth(new AuthData(authToken, "a"));

        var request = new LogoutRequest(authToken);
        var response = logoutService.logout(request);

        Assertions.assertNotNull(response);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }

    @Test
    public void logoutFail() { //uses wrong auth token
        var authDAO = new MemoryAuthDAO();
        var logoutService = new LogoutService(authDAO);
        var wrongAuthToken = "b";
        var logoutRequest = new LogoutRequest(wrongAuthToken);

        Assertions.assertThrows(ResponseException.class, () -> logoutService.logout(logoutRequest));
    }

    @Test
    public void createGameSuccess() throws ResponseException, DataAccessException {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var createGameService = new CreateGameService(authDAO, gameDAO);
        var authData = new AuthData("1", "a");
        authDAO.createAuth(authData);

        var request = new CreateGameRequest("1", "Game1");
        var response = createGameService.createGame(request);

        Assertions.assertNotNull(response);
    }

    @Test
    public void createGameFail() {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var createGameService = new CreateGameService(authDAO, gameDAO);
        var wrongAuth = new CreateGameRequest("2", "Game1");

        Assertions.assertThrows(ResponseException.class, () -> createGameService.createGame(wrongAuth));
    }

    @Test
    public void listGamesSuccess() throws ResponseException, DataAccessException {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var listGamesService = new ListGamesService(authDAO, gameDAO);
        var authData = new AuthData("1", "a");
        authDAO.createAuth(authData);

        var game1 = new GameData(1, "Game1", "user1", null, null);
        var game2 = new GameData(2, "Game2", null, "user2", null);
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);

        var listGamesResult = listGamesService.listGames("1");

        Assertions.assertNotNull(listGamesResult);
        Assertions.assertEquals(2, listGamesResult.games().size());
    }

    @Test
    public void listGamesFail() {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var listGamesService = new ListGamesService(authDAO, gameDAO);

        Assertions.assertThrows(ResponseException.class, () -> listGamesService.listGames("wrongAuthToken"));
    }

    @Test
    public void joinGameSuccess() throws ResponseException, DataAccessException {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var joinGameService = new JoinGameService(authDAO, gameDAO);
        var authData = new AuthData("1", "a");
        authDAO.createAuth(authData);

        var game = new GameData(1, null, "user2", "Game1", null);
        gameDAO.createGame(game);

        var joinRequest = new JoinGameRequest("1", "white", 1);
        var joinResult = joinGameService.joinGame(joinRequest);

        Assertions.assertNotNull(joinResult);
    }

    @Test
    public void joinGameFail() throws DataAccessException {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var joinGameService = new JoinGameService(authDAO, gameDAO);
        var game = new GameData(1, "Game1", "user1", null, null);
        gameDAO.createGame(game);

        var joinRequest = new JoinGameRequest("wrongAuthToken", "white", 1);

        Assertions.assertThrows(ResponseException.class, () -> joinGameService.joinGame(joinRequest));
    }

}
