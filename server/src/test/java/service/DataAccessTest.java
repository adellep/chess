package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    public void loginUser() throws ResponseException {
        var userDAO = new UserDAOMemory();
        var authDAO = new AuthDAOMemory();
        var loginService = new LoginService(userDAO, authDAO);

        //need a user to find
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
    public void logoutUser() throws ResponseException, DataAccessException {
        var authDAO = new AuthDAOMemory();
        var logoutService = new LogoutService(authDAO);

        var authToken = "1234";
        authDAO.createAuth(new AuthData(authToken, "a"));

        var logoutReq = new LogoutRequest(authToken);
        var logoutRes = logoutService.logout(logoutReq);

        Assertions.assertNotNull(logoutRes);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }
}