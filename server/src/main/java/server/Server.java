package server;

import com.google.gson.Gson;
import dataaccess.*;
import service.*;
import spark.*;

import java.io.Reader;

public class Server {

    private final UserDAO userDao;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;


    public Server() {
        this.userDao = new UserDAOMemory();
        this.gameDAO = new GameDAOMemory();
        this.authDAO = new AuthDAOMemory();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.delete("/db", this::clear);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createNewGame);
        Spark.get("/game", this::listAllGames);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(new Gson().toJson(new ResultMessage(ex.getMessage())));
    }

    private String clear(Request req, Response res) {
        ClearService clearService = new ClearService(this.userDao, this.gameDAO, this.authDAO);
        ResultMessage clearedResult = clearService.clear();

        if (clearedResult.message() == null) {
            res.status(200);
        } else {
            res.status(500);
        }

        var g = new Gson();
        return g.toJson(clearedResult);
    }

    private String createUser(Request req, Response res) throws ResponseException {
        var g = new Gson();
        var newUser = g.fromJson(req.body(), RegisterRequest.class);
        RegisterService registerService = new RegisterService(this.userDao, this.authDAO);
        var result = registerService.register(newUser);

        res.status(200);
        return g.toJson(result);
    }

    private String loginUser(Request req, Response res) throws ResponseException {
        var g = new Gson();
        var loginRequest = g.fromJson(req.body(), LoginRequest.class);
        var loginService = new LoginService(this.userDao, this.authDAO);
        var loginResult = loginService.login(loginRequest);

        res.status(200);
        return g.toJson(loginResult);
    }

    private String logoutUser(Request req, Response res) throws ResponseException {
        var g = new Gson();
        //var logoutRequest = g.fromJson(req.headers(), LogoutRequest.class);
        String authToken = req.headers("Authorization");

        var logoutRequest = new LogoutRequest(authToken);
        var logoutService = new LogoutService(this.authDAO);
        var logoutResult = logoutService.logout(logoutRequest);

        res.status(200);
        return g.toJson(logoutResult);
    }

    private String createNewGame(Request req, Response res) throws ResponseException {
        var g = new Gson();

        String authToken = req.headers("Authorization");
        String gameName = req.body();
        //String gameName = "game1";

        var createGameRequest = new CreateGameRequest(authToken, gameName);
        var createGameService = new CreateGameService(this.authDAO, this.gameDAO);

        try {
            var createGameResult = createGameService.createGame(createGameRequest);

            res.status(200);
            return g.toJson(createGameResult);

        } catch (ResponseException ex) {
            res.status(ex.StatusCode());
            return g.toJson(new ResultMessage(ex.getMessage()));
        }
    }

    private String listAllGames(Request req, Response res) throws ResponseException, DataAccessException {
        var g = new Gson();

        String authToken = req.headers("Authorization");

        var listGamesService = new ListGamesService(this.authDAO, this.gameDAO);

        try {
            var listGamesResult = listGamesService.listGames(authToken);

            res.status(200);
            return g.toJson(listGamesResult);

        } catch (ResponseException ex) {
            res.status(ex.StatusCode());
            return g.toJson(new ResultMessage(ex.getMessage()));
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
