package server;

import com.google.gson.Gson;
import model.UserData;
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
        Spark.delete("/db", (req, response) -> "{}"); //clear()

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String createUser(Request req, Response res) {
        var g = new Gson();
        var newUser = g.fromJson("{ username:, password:, email: }", UserData.class);
        return "{ username:, password:, email: }";



//        var x = s.registerUser(newUser);
//        return g.toJson(x);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
