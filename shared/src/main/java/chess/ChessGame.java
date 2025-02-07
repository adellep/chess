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

    private ChessBoard board;
    private TeamColor teamTurn;

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
    //return all moves a piece can legally make
    //if no piece = null
    //move is valid if it is a "piece move" for the piece at the input location and
    // making that move would not leave the team’s king in danger of check
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        TeamColor teamColorTurn = piece.getTeamColor();
        Collection<ChessMove> possMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : possMoves) {
            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());

            board.addPiece(move.getEndPosition(), piece); //move piece and clear old pos
            board.addPiece(startPosition, null);

            if (!isInCheck(teamColorTurn)) {
                validMoves.add(move);
            }

            board.addPiece(startPosition, piece); //put both back in orig pos
            board.addPiece(move.getEndPosition(), capturedPiece);
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    //throw exception if piece is null, if teamColor doesn't match teamTurn, if move isn't in validMoves
    //if piece is a pawn and row = 1 or 8, replace with promoted piece
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            throw new InvalidMoveException("no piece at this position");
        }
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("not your turn");
        }

        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("invalid move for this piece");
        }

        ChessPosition endPosition = move.getEndPosition();
        board.addPiece(endPosition, piece); //move piece and make old position empty
        board.addPiece(startPosition, null);

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (endPosition.getRow() == 1 || endPosition.getRow() == 8) {
                ChessPiece promotedPawn = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(endPosition, null);
                board.addPiece(endPosition, promotedPawn);
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    //king could be attacked by another piece
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = kingPosition(teamColor);

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    for (ChessMove moves : piece.pieceMoves(board, pos)) {
                        if (moves.getEndPosition().equals(kingPos)) {
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

                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
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
    //when king is in check and can't escape
    //store piece that might get captured, but put back after simulated move
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessPiece> teamPieces = getAllTeamPieces(teamColor);
        for (ChessPiece piece : teamPieces) {
            ChessPosition piecePos = getPosition(piece);
            Collection<ChessMove> moves = piece.pieceMoves(board, piecePos);

            for (ChessMove move : moves) {
                ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), piece); //move the piece
                board.addPiece(piecePos, null); //clear the old piece pos

                boolean stillChecked = isInCheck(teamColor);

                board.addPiece(piecePos, piece); //put piece back in original pos
                board.addPiece(move.getEndPosition(), capturedPiece); //put captured piece back

                if (!stillChecked) {
                    return false;
                }
            }
        }
        return true;
    }

    private Collection<ChessPiece> getAllTeamPieces(TeamColor teamColor) {
        Collection<ChessPiece> teamPieces = new ArrayList<>();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    teamPieces.add(piece);
                }
            }
        }
        return teamPieces;
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
    //king is not in check, and no legal move available
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessPiece> teamPieces = getAllTeamPieces(teamColor);
        for (ChessPiece piece : teamPieces) {
            ChessPosition piecePos = getPosition(piece);
            Collection<ChessMove> moves = piece.pieceMoves(board, piecePos);

            for (ChessMove move : moves) {
                ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), piece); //move the piece
                board.addPiece(piecePos, null); //clear old piece pos

                boolean stillChecked = isInCheck(teamColor);

                board.addPiece(piecePos, piece); //put piece back
                board.addPiece(move.getEndPosition(), capturedPiece); //put captured piece back

                if (!stillChecked) {
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
