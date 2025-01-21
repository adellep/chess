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
        //return new ArrayList<>();
        var moves = new ArrayList<ChessMove>();
        var piece = board.getPiece(myPosition);

        if(piece.getPieceType() == PieceType.BISHOP) {
            getBishopMoves(moves, myPosition, myPosition, -1, -1, true);
            getBishopMoves(moves, myPosition, myPosition, +1, -1, true);
            getBishopMoves(moves, myPosition, myPosition, +1, +1, true);
            getBishopMoves(moves, myPosition, myPosition, -1, +1, true);
        }
        return moves;
    }

    private void getBishopMoves(Collection<ChessMove> moves, ChessPosition originalPos, ChessPosition pos, int rowDir, int colDir, boolean keepMoving) {

        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);
        moves.add(new ChessMove(originalPos, newPos, null));

        if(keepMoving && isRealSquare(newPos)) {
            getBishopMoves(moves, originalPos, newPos, rowDir, colDir, keepMoving);
        }
    }

    boolean isRealSquare(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();

        return row >= 0 && row <= 8 && col >= 0 && col <= 8;
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
