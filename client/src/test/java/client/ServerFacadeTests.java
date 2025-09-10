package client;

import model.UserData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import server.Server;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDatabase() throws ResponseException {
        facade.clear();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void clearTest() {
        Assertions.assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    public void registerUserSuccess() {
        var user = new UserData("a", "p", "a@a.com");
        var res = Assertions.assertDoesNotThrow(() -> facade.register(user));

        Assertions.assertNotNull(res);
        Assertions.assertEquals("a", res.username());
        Assertions.assertNotNull(res.authToken());
    }

    @Test
    public void registerUserAlreadyExists() {
        var user = new UserData("a", "p", "a@a.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));

        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> facade.register(user));
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void loginUserSuccess() {
        var user = new UserData("a", "p", "a@a.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));

        var req = new LoginRequest("a", "p");
        var res = Assertions.assertDoesNotThrow(() -> facade.login(req));

        Assertions.assertNotNull(res);
        Assertions.assertEquals("a", res.username());
        Assertions.assertNotNull(res.authToken());
    }

    @Test
    public void loginUserWrongPassword() {
        var user = new UserData("a", "p", "a@a.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));

        var req = new LoginRequest("a", "wongPass");
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> facade.login(req));
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void logoutUserSuccess() {
        var user = new UserData("a", "p", "a@a.com");
        var res = Assertions.assertDoesNotThrow(() -> facade.register(user));
        String authToken = res.authToken();

        Assertions.assertDoesNotThrow(() -> facade.logout(authToken));
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> facade.listGames(authToken));
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void logoutWrongAuthToken() {
        String fakeAuthToken = "fakeAuth";
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> facade.logout(fakeAuthToken));
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void createGameSuccess() {
        var user = new UserData("a", "p", "a@a.com");
        var res = Assertions.assertDoesNotThrow(() -> facade.register(user));
        String authToken = res.authToken();
        var createReq = new CreateGameRequest(authToken, "Created Game");
        var createRes = Assertions.assertDoesNotThrow(() -> facade.createGame(createReq));

        Assertions.assertTrue(createRes.gameID() > 0);
    }

    @Test
    public void createGameFailure() {
        String fakeAuthToken = "fakeAuth";
        var createReq = new CreateGameRequest(fakeAuthToken, "Bad Game");
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> facade.createGame(createReq));

        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void listGamesSuccess() {
        var user = new UserData("a", "p", "a@a.com");
        var registerRes = Assertions.assertDoesNotThrow(() -> facade.register(user));
        String authToken = registerRes.authToken();
        Assertions.assertDoesNotThrow(() -> facade.createGame(new CreateGameRequest(authToken, "Game 1")));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new CreateGameRequest(authToken, "Game 2")));

        var listRes = Assertions.assertDoesNotThrow(() -> facade.listGames(authToken));

        Assertions.assertNotNull(listRes);
        Assertions.assertEquals(2, listRes.games().size());
    }

    @Test
    public void listGamesWrongAuthToken() {
        String fakeAuthToken = "fakeAuth";
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> facade.listGames(fakeAuthToken));
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void joinGameSuccess() {
        var user = new UserData("a", "p", "a@a.com");
        var registerRes = Assertions.assertDoesNotThrow(() -> facade.register(user));
        String authToken = registerRes.authToken();
        var createRes = Assertions.assertDoesNotThrow(() -> facade.createGame(new CreateGameRequest(authToken, "Joined Game")));
        int gameID = createRes.gameID();
        var joinReq = new JoinGameRequest(authToken, "WHITE", gameID);
        Assertions.assertDoesNotThrow(() -> facade.joinGame(joinReq));

        var listRes = Assertions.assertDoesNotThrow(() -> facade.listGames(authToken));
        Assertions.assertEquals("a", listRes.games().get(0).whiteUsername());
    }

    @Test

    public void joinGameSpotTaken() {
        var userA = new UserData("a", "p", "a@a.com");
        var userB = new UserData("b", "q", "b@mail.com");
        var regA = Assertions.assertDoesNotThrow(() -> facade.register(userA));
        var regB = Assertions.assertDoesNotThrow(() -> facade.register(userB));

        var createReq = new CreateGameRequest(regA.authToken(), "New Game");
        var createRes = Assertions.assertDoesNotThrow(() -> facade.createGame(createReq));
        int gameID = createRes.gameID();

        var joinA = new JoinGameRequest(regA.authToken(), "BLACK", gameID);
        Assertions.assertDoesNotThrow(() -> facade.joinGame(joinA));

        var joinB = new JoinGameRequest(regB.authToken(), "BLACK", gameID);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(joinB));
        Assertions.assertEquals(500, exception.getStatusCode());
    }
}
