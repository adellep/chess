package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.AuthData;

public class LogoutService {

    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        try {
            authDAO.deleteAuth(request.authToken());

            return new LogoutResult("logged out");

        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: error messsage");
        }
    }
}
