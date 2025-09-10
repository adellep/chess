package ui;

import chess.ChessGame;
import client.ResponseException;
import client.ServerFacade;
import model.GameData;
import model.UserData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Repl {
    private final ServerFacade serverFacade;
    private String authToken = null;
    private boolean loggedIn = false;
    private List<GameData> gameList = new ArrayList<>();
    private final ChessboardPrint printBoard = new ChessboardPrint();

    public Repl(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to 240 chess. Type Help to get started.");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printPrompt();
            String line = scanner.nextLine();
            var words = line.split(" ");
            var command = words[0];
            var params = Arrays.copyOfRange(words, 1, words.length);

            try {
                if (loggedIn) {
                    switch (command.toLowerCase()) {
                        case "logout":
                            logout();
                            break;
                        case "create":
                            createGame(params);
                            break;
                        case "list":
                            listGames();
                            break;
                        case "help":
                            displayHelp();
                            break;
                        case "quit":
                            System.out.println("Exiting Chess.");
                            return;
                        case "join":
                            joinGame(params);
                            break;
                        case "observe":
                            observeGame(params);
                            break;
                        default:
                            System.out.println("Unknown command. Type 'help' to see available commands.");
                    }
                } else {
                    switch (command.toLowerCase()) {
                        case "register":
                            register(params);
                            break;
                        case "login":
                            login(params);
                            break;
                        case "help":
                            displayHelp();
                            break;
                        case "quit":
                            System.out.println("Exiting Chess.");
                            return;
                        default:
                            System.out.println("Unknown command. Type 'help' to see available commands.");
                    }
                }
            } catch (ResponseException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    public void logout() throws ResponseException {
        serverFacade.logout(authToken);
        this.loggedIn = false;
        this.authToken = null;
        this.gameList.clear();
        System.out.println("You have been logged out.");
    }

    public void createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            String gameName = params[0];
            var createReq = new CreateGameRequest(authToken, gameName);
            serverFacade.createGame(createReq);

            System.out.println("Game '" + gameName + "' created.");
        } else {
            System.out.println("Invalid command. Help: create <GAME_NAME>");
        }
    }

    public void listGames() throws ResponseException {
        var res = serverFacade.listGames(authToken);
        this.gameList = new ArrayList<>(res.games());

        System.out.println("Games:");
        if (gameList.isEmpty()) {
            System.out.println("  No games available.");
        } else {
            for (int i = 0; i < gameList.size(); i++) {
                var game = gameList.get(i);
                System.out.printf("  %d. %s (White: %s, Black: %s)\n",
                        i + 1,
                        game.gameName(),
                        game.whiteUsername() != null ? game.whiteUsername() : "available",
                        game.blackUsername() != null ? game.blackUsername() : "available");
            }
        }
    }

    public void register(String... params) throws ResponseException {
        if (params.length == 3) {
            var userData = new UserData(params[0], params[1], params[2]);
            var registerRes = serverFacade.register(userData);
            this.authToken = registerRes.authToken();
            this.loggedIn = true;
            System.out.println("Registered and logged in as " + registerRes.username());
        } else {
            System.out.println("Invalid command. Help: register <USERNAME> <PASSWORD> <EMAIL>");
        }
    }

    public void login(String... params) throws ResponseException {
        if (params.length == 2) {
            var req = new LoginRequest(params[0], params[1]);
            var res = serverFacade.login(req);
            this.authToken = res.authToken();
            this.loggedIn = true;
            System.out.println("Logged in as " + res.username());
        } else {
            System.out.println("Invalid command. Help: login <USERNAME> <PASSWORD>");
        }
    }

    public void displayHelp() {
        if (loggedIn) {
            System.out.println("""
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE|BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """);
        } else {
            System.out.println("""
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """);
        }
    }

    private void printPrompt() {
        if (loggedIn) {
            System.out.print("\n[LOGGED_IN] >>> ");
        } else {
            System.out.print("\n[LOGGED_OUT] >>> ");
        }
    }

    public void joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            try {
                int gameNumber = Integer.parseInt(params[0]);
                int gameID = gameList.get(gameNumber - 1).gameID();
                String playerColorStr = params[1].toUpperCase();

                ChessGame.TeamColor playerColor;
                if (playerColorStr.equals("WHITE")) {
                    playerColor = ChessGame.TeamColor.WHITE;
                } else if (playerColorStr.equals("BLACK")) {
                    playerColor = ChessGame.TeamColor.BLACK;
                } else {
                    System.out.println("Choose a color to join (WHITE or BLACK).");
                    return;
                }
                serverFacade.joinGame(new JoinGameRequest(authToken, playerColorStr, gameID));
                System.out.println("Successfully joined game " + gameNumber + " as " + playerColor + ".");

                listGames();
                GameData joinedGame = gameList.get(gameNumber - 1);
                printBoard.printBoard(joinedGame.game(), playerColor);

            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Error: Provide valid game number.");
            }
        } else {
            System.out.println("Invalid command. Help: join <GAME_NUMBER> [WHITE|BLACK]");
        }
    }

    public void observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            try {
                int gameNumber = Integer.parseInt(params[0]);
                int gameID = gameList.get(gameNumber - 1).gameID();
                serverFacade.joinGame(new JoinGameRequest(authToken, "OBSERVER", gameID));
                System.out.println("Successfully observing game " + gameNumber + ".");

                var observedGame = gameList.get(gameNumber - 1);
                printBoard.printBoard(observedGame.game(), ChessGame.TeamColor.WHITE);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Error: Provide a valid game number.");
            }
        } else {
            System.out.println("Invalid command. Help: observe <GAME_NUMBER>");
        }
    }
}