package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var piece = board.getPiece(myPosition);

        if (piece.getPieceType() == PieceType.BISHOP) {
            moves = new BishopMoves().moves(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.KNIGHT) {
            moves = new KnightMoves().moves(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.ROOK) {
            moves = new RookMoves().moves(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.QUEEN) {
            moves = new RookMoves().moves(board, myPosition);
            moves.addAll(new BishopMoves().moves(board, myPosition));
        }
        if (piece.getPieceType() == PieceType.KING) {
            moves = new KingMoves().moves(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.PAWN) {
            moves = new PawnMoves().moves(board, myPosition);
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
