package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import result.ClearResult;

public class ClearService {
    final private UserDAO userDAO;
    final private GameDAO gameDAO;
    final private AuthDAO authDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ClearResult clear() {
        try {
            userDAO.clear();
            gameDAO.clear();;
            authDAO.clear();
            return new ClearResult(null);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
