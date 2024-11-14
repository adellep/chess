package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import model.AuthData;
import requests.LogoutRequest;

public class LogoutService {

    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public ResultMessage logout(LogoutRequest request) throws ResponseException {
        try {

            var authToken = authDAO.getAuth(request.authToken());

            if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            authDAO.deleteAuth(request.authToken());

            return new ResultMessage("logged out");

        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
