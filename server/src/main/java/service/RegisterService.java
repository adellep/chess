package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    //public AuthData register(UserData user) {
    public RegisterResult register(RegisterRequest request) throws ResponseException {

        if (request.username() == null || request.password() == null || request.email() == null ||
                request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
            throw new ResponseException(400, "Error: bad request");
        }

        try {
            UserData foundUser = userDAO.getUser(request.username());

            //user already exists
            if (foundUser != null) {
                throw new ResponseException(403, "Error: already taken");
            }

            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.addUser(newUser);

            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, newUser.username());
            authDAO.createAuth(authData);

            return new RegisterResult(newUser.username(), authToken);

        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: error message"); //get error message
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
