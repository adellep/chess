package service;

import dataaccess.AuthDAOMemory;
import dataaccess.DataAccessException;
import dataaccess.UserDAOMemory;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataAccessTest {

    //example test from class
//    @Test
//    public void registerUser() {
//        var dataAccess = new MemoryDataAccess();
//        var expected = new UserData("a", "p", "a@a.com");
//        dataAccess.addUser(expected);
//        var actual = dataAccess.getUser("a");
//        Assertions.assertEquals(expected, actual);
//    }

    @Test
    public void clearTest() {
        var clearService = new ClearService();
        var result = clearService.clear();
        Assertions.assertNull(result.message());
    }

    @Test
    public void registerUser() throws DataAccessException {
        var userDao = new UserDAOMemory();
        var authDao = new AuthDAOMemory();
        //var userService = new UserService(userDao);
        var registerService = new RegisterService(userDao, authDao);
        var req = new RegisterRequest("a", "p", "a@a.com");
        //AuthData authData = userService.register(expected);
        var res = registerService.register(req);

        Assertions.assertNotNull(res);
        Assertions.assertEquals("a", res.username());
        Assertions.assertNotNull(res.authToken());
        Assertions.assertNotNull(userDao.getUser("a"));
    }
}