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
            throw new RuntimeException(ex);
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
