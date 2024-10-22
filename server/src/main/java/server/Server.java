package server;

import com.google.gson.Gson;
import dataaccess.*;
import service.*;
import spark.*;

public class Server {
//    private final Service s; //class code
//
//    public Server(Service s) {
//        this.s = s;
//    }
    private final UserDAO userDao;
    private final AuthDAO authDAO;

    public Server() {
        this.userDao = new UserDAOMemory();
        this.authDAO = new AuthDAOMemory();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.delete("/db", this::clear);
        Spark.post("/session", this::loginUser);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(new Gson().toJson(new ResultMessage(ex.getMessage()))); //why does this work to pass register bad request?
    }

    private String clear(Request req, Response res) {
        ClearService clearService = new ClearService();
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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
