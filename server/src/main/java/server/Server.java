package server;

import com.google.gson.Gson;
import dataaccess.*;
import request.*;
import result.ClearResult;
import service.*;
import spark.*;

public class Server {

    final private UserDAO userDAO;
    final private GameDAO gameDAO;
    final private AuthDAO authDAO;

    public Server() {
        this.userDAO = new MemoryUserDAO();
        this.gameDAO = new MemoryGameDAO();
        this.authDAO = new MemoryAuthDAO();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::createUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);

        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException e, Request request, Response response) {
        response.status(e.getStatusCode());
        response.body(new Gson().toJson(new ClearResult(e.getMessage())));
    }

    private String clear(Request request, Response response) {
        ClearService clearService = new ClearService(this.userDAO, this.gameDAO, this.authDAO);
        ClearResult clearResult = clearService.clear();

        if (clearResult.message() == null) {
            response.status(200);
        } else {
            response.status(500);
        }
        var g = new Gson();
        return g.toJson(clearResult);
    }

    private String createUser(Request request, Response response) throws ResponseException {
        var g = new Gson();
        var newUser = g.fromJson(request.body(), RegisterRequest.class);
        RegisterService registerService = new RegisterService(this.userDAO, this.authDAO);
        var result = registerService.register(newUser);

        response.status(200);
        return g.toJson(result);
    }

    private String loginUser(Request request, Response response) throws ResponseException {
        var g = new Gson();
        var loginRequest = g.fromJson(request.body(), LoginRequest.class);
        var loginService = new LoginService(this.userDAO, this.authDAO);
        var loginResult = loginService.login(loginRequest);

        response.status(200);
        return g.toJson(loginResult);
    }

    private String logoutUser(Request request, Response response) throws ResponseException {
        var g = new Gson();
        //var logoutRequest = g.fromJson(request.body(), LogoutRequest.class);
        String authToken = request.headers("Authorization");
        var logoutRequest = new LogoutRequest(authToken);
        var logoutService = new LogoutService(this.authDAO);
        var logoutResult = logoutService.logout(logoutRequest);

        response.status(200);
        return g.toJson(logoutResult);
    }

    private String createGame(Request request, Response response) throws ResponseException {
        var g = new Gson();
        String authToken = request.headers("Authorization");
        String gameName = request.body();
        var createGameRequest = new CreateGameRequest(authToken, gameName);
        var createGameService = new CreateGameService(this.authDAO, this.gameDAO);

        try {
            var createGameResult = createGameService.createGame(createGameRequest);

            response.status(200);
            return g.toJson(createGameResult);
        } catch (ResponseException e) {
            response.status(e.getStatusCode());

            return g.toJson(new ClearResult(e.getMessage()));
        }
    }

    private String listGames(Request request, Response response) throws ResponseException, DataAccessException {
        var g = new Gson();
        String authToken = request.headers("Authorization");
        var listGamesService = new ListGamesService(this.authDAO, this.gameDAO);

        try {
            var listGamesResult = listGamesService.listGames(authToken);

            response.status(200);
            return g.toJson(listGamesResult);
        } catch (ResponseException e) {
            response.status(e.getStatusCode());
            return g.toJson(new ClearResult(e.getMessage()));
        }
    }

    private String joinGame(Request request, Response response) throws ResponseException {
        var g = new Gson();
        String authToken = request.headers("Authorization");
        var body = g.fromJson(request.body(), JoinGameRequest.class);
        var joinGameService = new JoinGameService(this.authDAO, this.gameDAO);

        try {
            var joinGameResult = joinGameService.joinGame(new JoinGameRequest(authToken, body.playerColor(), body.gameID()));

            response.status(200);
            return g.toJson(joinGameResult);
        } catch (ResponseException e) {
            response.status(e.getStatusCode());
            return g.toJson(new ClearResult(e.getMessage()));
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
