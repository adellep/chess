package service;

import dataaccess.*;

public class ClearService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ClearService() {
        this.userDAO = new UserDAOMemory();
        this.gameDAO = new GameDAOMemory();
        this.authDAO = new AuthDAOMemory();
    }

    public ClearResult clear() {
        try {
            userDAO.clear();
            gameDAO.clear();
            authDAO.clear();

            return new ClearResult(null);
        } catch (Exception ex) {
            return new ClearResult("not cleared");
        }
    }
}
