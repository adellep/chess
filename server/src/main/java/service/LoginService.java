package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;

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

            if (foundUser == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            if (!Objects.equals(foundUser.password(), request.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            String authToken = "1234";
            AuthData authData = new AuthData(authToken, foundUser.username());
            authDAO.createAuth(authData);

            return new LoginResult(foundUser.username(), authToken);

        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: error messsage"); //need to return actual error message
        }
    }

}