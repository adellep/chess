package service;

import dataaccess.*;

import service.ResultMessage; // Ensure this import is correct

public class ClearService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ResultMessage clear() {
        try {
            userDAO.clear();
            gameDAO.clear();
            authDAO.clear();

            return new ResultMessage(null);
        } catch (Exception ex) {
            return new ResultMessage("not cleared");
        }
    }
}
