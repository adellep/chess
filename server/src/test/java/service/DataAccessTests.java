package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
}
