package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;

import java.util.Objects;
import java.util.UUID;

public class LoginService {

    final private UserDAO userDAO;
    final private AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        try {
            UserData userFound = userDAO.getUser(request.username());

            if (userFound == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            if (!Objects.equals(userFound.password(), request.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, userFound.username());
            authDAO.createAuth(authData);

            return new LoginResult(userFound.username(), authToken);
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
