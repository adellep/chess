package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
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
//old comparison: !Objects.equals(userFound.password(), request.password())
    public LoginResult login(LoginRequest request) throws ResponseException {
        try {
            UserData userFound = userDAO.getUser(request.username());

            if (userFound == null || !BCrypt.checkpw(request.password(), userFound.password())) {
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
