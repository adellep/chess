package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
    //returns all moves the piece at position can make
    //move is valid if it is a "piece move" for the piece at the input location and
    //making that move would not leave the team’s king in danger of check
    //call pieceMoves on startPosition
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        TeamColor teamColorTurn = piece.getTeamColor();
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : possibleMoves) {
            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());

            board.addPiece(move.getEndPosition(), piece); //move piece
            board.addPiece(startPosition, null); //clear old pos

            if (!isInCheck(teamColorTurn)) { //can only add moves if not in check after
                validMoves.add(move);
            }

            board.addPiece(startPosition, piece); //put in orig pos
            board.addPiece(move.getEndPosition(), capturedPiece); //put captured back
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    //receives a given move and executes it, provided it is a legal move.
    //if illegal, it throws an InvalidMoveException.
    //move is illegal if it is not a "valid" move for the piece at the starting location,
    //or if it’s not the corresponding team's turn.
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPiece piece = board.getPiece(startPos);

        if (piece == null) {
            throw new InvalidMoveException("No piece at position");
        }
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not your turn");
        }

        Collection<ChessMove> validMoves = validMoves(startPos);
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move for piece");
        }

        ChessPosition endPos = move.getEndPosition();
        board.addPiece(endPos, piece); //move piece
        board.addPiece(startPos, null); //make old pos empty

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (endPos.getRow() == 1 || endPos.getRow() == 8) {
                ChessPiece promotedPawn = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(endPos, null);
                board.addPiece(endPos, promotedPawn);
            }
        }

        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
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

                if (pieceThreatened(piece, teamColor, pos, kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean pieceThreatened(ChessPiece piece, TeamColor teamColor, ChessPosition pos, ChessPosition kingPos) {
        if (piece == null || piece.getTeamColor() == teamColor) {
            return false;
        }

        for (ChessMove move : piece.pieceMoves(board, pos)) {
            if (move.getEndPosition().equals(kingPos)) {
                return true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
