package server;

import com.google.gson.Gson;
import dataaccess.*;
import result.ClearResult;
import service.ClearService;
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

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
