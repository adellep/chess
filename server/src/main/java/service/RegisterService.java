package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.UUID;

public class RegisterService {
    //implement logic for register endpoint

    final private UserDAO userDAO;
    final private AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        //throw new DataAccessException("Error 400");
        if (request.username() == null || request.password() == null || request.email() == null ||
                request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
            throw new ResponseException(400, "Error: bad request");
        }
        try {
            UserData userFound = userDAO.getUser(request.username());
            if (userFound != null) {
                //throw new DataAccessException("Error 403");
                throw new ResponseException(403, "Error: already taken");
            }

            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.addUser(newUser);

            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, newUser.username());
            authDAO.createAuth(authData);

            return new RegisterResult(newUser.username(), authToken);
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
