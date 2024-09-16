package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        var piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            getKingMove(moves, myPosition, myPosition, +1, -1, board);
            getKingMove(moves, myPosition, myPosition, +1, 0, board);
            getKingMove(moves, myPosition, myPosition, +1, +1, board);
            getKingMove(moves, myPosition, myPosition, 0, +1, board);

            getKingMove(moves, myPosition, myPosition, -1, +1, board);
            getKingMove(moves, myPosition, myPosition, -1, 0, board);
            getKingMove(moves, myPosition, myPosition, -1, -1, board);
            getKingMove(moves, myPosition, myPosition, 0, -1, board);
        }
        return moves;
    }

    boolean isRealSquare(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();

        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    boolean isEnemy(ChessPosition origPos, ChessPosition newPos, ChessBoard board) {
        ChessPiece pieceOrigPos = board.getPiece(origPos);
        ChessPiece pieceNewPos = board.getPiece(newPos);

        if (pieceNewPos == null) {
            return false;
        }

        return pieceNewPos.getTeamColor() != pieceOrigPos.getTeamColor();
    }

    void getKingMove(Collection<ChessMove> moves, ChessPosition origPos,
                     ChessPosition pos, int rowDir, int colDir, ChessBoard board) {

        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);

        if (!isRealSquare(newPos)) {
            return;
        }
        if (isEnemy(origPos, newPos, board)) {
            moves.add(new ChessMove(origPos, newPos, null));
        }
        else if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(origPos, newPos, null));
        }
    }
}
