package server;

import com.google.gson.Gson;
import dataaccess.UserDAO;
import dataaccess.UserDAOMemory;
import model.UserData;
import service.*;
import service.Service;
import spark.*;

public class Server {
//    private final Service s; //class code
//
//    public Server(Service s) {
//        this.s = s;
//    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser); //was lambda function

        Spark.delete("/db", this::clear); //clear() //(req, response) -> "{}"

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String clear(Request req, Response res) {
        ClearService clearService = new ClearService();
        ClearResult clearedResult = clearService.clear();

        if (clearedResult.message() == null) {
            res.status(200);
        } else {
            res.status(500);
        }

        var g = new Gson();
        return g.toJson(clearedResult);
    }

    private String createUser(Request req, Response res) {
        var g = new Gson();
        var newUser = g.fromJson(req.body(), UserData.class);
        //return "{ username:, password:, email: }";
//        var x = s.registerUser(newUser);
//        return g.toJson(x);
        var userService = new UserService();
        var authData = userService.register(newUser);

        if (authData != null) {
            res.status(200);
            return g.toJson(new RegisterResult(newUser.username(), authData.authToken()));
        } else {
            res.status(403);
            return g.toJson(new ClearResult("Error : already taken"));
        }

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
