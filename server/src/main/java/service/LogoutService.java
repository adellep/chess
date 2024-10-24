package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import model.AuthData;

public class LogoutService {

    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        try {

            var authToken = authDAO.getAuth(request.authToken());

            if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            authDAO.deleteAuth(request.authToken());

            return new LogoutResult("logged out");

        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: error messsage");
        }
    }
}
