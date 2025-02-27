package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;

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
}
