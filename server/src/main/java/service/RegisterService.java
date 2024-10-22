package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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
    public RegisterResult register(RegisterRequest request) {

        if (request.username() == null || request.password() == null || request.email() == null ||
                request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
            return null; //can i put error message here instead of server?
        }

        try {
            UserData foundUser = userDAO.getUser(request.username());

            //user already exists
            if (foundUser != null) {
                return null;
            }

            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.addUser(newUser);
            //String authToken = "hardCodedAuthTok4884";
            //return new AuthData(authToken, newUser.username());
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, newUser.username());
            authDAO.createAuth(authData);

            return new RegisterResult(newUser.username(), authToken);

        } catch (DataAccessException ex) {
            //throw new RuntimeException(ex);
            return null;
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
