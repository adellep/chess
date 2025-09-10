package client;

import com.google.gson.Gson;
import model.UserData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import results.CreateGameResult;
import results.ListGamesResult;
import results.LoginResult;
import results.RegisterResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public RegisterResult register(UserData userData) throws ResponseException {
        var path = "/user";

        return this.makeRequest("POST", path, userData, RegisterResult.class, null);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class, request.authToken());
    }

    public ListGamesResult listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResult.class, authToken);
    }

    public void joinGame(JoinGameRequest request) throws ResponseException {
        var path = "/game";
        String authToken = request.authToken();
        this.makeRequest("PUT", path, request, null, authToken);
    }

    private <T> T makeRequest(String method, String path, Object requestBody, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
            if (requestBody != null) {
                http.addRequestProperty("Content-Type", "application/json");
                String jsonBody = new Gson().toJson(requestBody);
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(jsonBody.getBytes());
                }
            }
            http.connect();
            if (http.getResponseCode() / 100 != 2) {
                String errorMess;
                try (InputStream errorStream = http.getErrorStream()) {
                    if (errorStream != null) {
                        errorMess = new String(errorStream.readAllBytes());
                    } else {
                        errorMess = http.getResponseMessage();
                    }
                }
                throw new ResponseException(http.getResponseCode(), errorMess);
            }

            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    return new Gson().fromJson(reader, responseClass);
                }
                return null;
            }
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
