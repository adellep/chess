package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
