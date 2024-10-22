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
