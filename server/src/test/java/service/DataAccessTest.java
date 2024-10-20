package service;

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
}