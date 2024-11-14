package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import results.LoginResult;

import java.util.Objects;
import java.util.UUID;

public class LoginService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        try {
            UserData foundUser = userDAO.getUser(request.username());

            if (foundUser == null || !Objects.equals(foundUser.password(), request.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, foundUser.username());
            authDAO.createAuth(authData);

            return new LoginResult(foundUser.username(), authToken);

        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
