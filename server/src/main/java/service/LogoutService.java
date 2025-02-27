package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import request.LogoutRequest;
import result.LogoutResult;

public class LogoutService {

    final private AuthDAO authDAO;

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
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
