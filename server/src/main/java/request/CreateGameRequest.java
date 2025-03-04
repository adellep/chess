package request;

public record CreateGameRequest(String authToken, String gameName) {
    public String getGameName() {
        return gameName;
    }
}
