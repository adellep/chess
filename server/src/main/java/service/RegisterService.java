package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requests.RegisterRequest;
import results.RegisterResult;

import java.util.UUID;

public class RegisterService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {

        if (request.username() == null || request.password() == null || request.email() == null ||
                request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
            throw new ResponseException(400, "Error: bad request");
        }

        try {
            UserData foundUser = userDAO.getUser(request.username());

            if (foundUser != null) {
                throw new ResponseException(403, "Error: already taken");
            }

            String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
            UserData newUser = new UserData(request.username(), hashedPassword, request.email());
            userDAO.addUser(newUser);

            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, newUser.username());
            authDAO.createAuth(authData);

            return new RegisterResult(newUser.username(), authToken);

        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
