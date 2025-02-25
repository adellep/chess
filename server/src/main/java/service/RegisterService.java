package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null | request.email() == null) {
            throw new DataAccessException("Error 400");
        }
        try {
            UserData userFound = userDAO.getUser(request.username());
            if (userFound != null) {
                throw new DataAccessException("Error 403");
            }

            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.addUser(newUser);

            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, newUser.username());
            authDAO.createAuth(authData);

            return new RegisterResult(newUser.username(), authToken);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error 500");
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
