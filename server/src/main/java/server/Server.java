package server;

import com.google.gson.Gson;
import dataaccess.AuthDAOMemory;
import dataaccess.UserDAOMemory;
import model.UserData;
import service.*;
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
        var newUser = g.fromJson(req.body(), RegisterRequest.class);

        RegisterService registerService = new RegisterService(new UserDAOMemory(), new AuthDAOMemory());
        var result = registerService.register(newUser);
        //var userService = new UserService(new UserDAOMemory());

        //var authData = RegisterService.register(newUser);

        if (result != null) {
            res.status(200);
            return g.toJson(result);
        } else {
            res.status(403);
            return g.toJson(new ClearResult("Error : already taken"));
        }
        //2 other errors: 400 and 500

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
