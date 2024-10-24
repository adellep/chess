package service;

import dataaccess.*;

public class ClearService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO; //new UserDAOMemory();
        this.gameDAO = gameDAO; //GameDAOMemory();
        this.authDAO = authDAO; //AuthDAOMemory();
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
