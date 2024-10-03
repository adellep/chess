package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    //in check: when king could be attacked by another piece
    //Returns true if the specified team’s King could be captured by an opposing piece
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = kingPosition(teamColor);

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(board, pos);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition kingPosition(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    //Returns true if the given team has no way to protect their king from being captured
    //no valid moves and is in check
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessPiece> teamPieces = getTeamPieces(teamColor);
        for (ChessPiece piece : teamPieces) {
            ChessPosition piecePos = getPosition(piece);
            Collection<ChessMove> moves = piece.pieceMoves(board, piecePos);

            for (ChessMove move : moves) {
                ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), piece); //move piece
                board.addPiece(piecePos, null); //clear old pos

                boolean stillChecked = isInCheck(teamColor);

                board.addPiece(piecePos, piece); //put in orig pos
                board.addPiece(move.getEndPosition(), capturedPiece); //put captured back

                if (!stillChecked) {
                    return false;
                }
            }
        }
        return true;
    }

    private Collection<ChessPiece> getTeamPieces(TeamColor teamColor) {
        Collection<ChessPiece> pieces = new ArrayList<>();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    private ChessPosition getPosition(ChessPiece piece) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);

                if (board.getPiece(pos) == piece) {
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    //Returns true if the given team has no legal moves but their king is not in immediate danger
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessPiece> teamPieces = getTeamPieces(teamColor);
        for (ChessPiece piece : teamPieces) {
            ChessPosition piecePos = getPosition(piece);
            Collection<ChessMove> moves = piece.pieceMoves(board, piecePos);

            for (ChessMove move : moves) {
                ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), piece);
                board.addPiece(piecePos, null);

                boolean nowIsChecked = isInCheck(teamColor);

                board.addPiece(piecePos, piece);
                board.addPiece(move.getEndPosition(), capturedPiece);

                if (!nowIsChecked) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
