package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoves extends CommonMoveRules {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();
        var piece = board.getPiece(myPosition);

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pawnMoves(moves, myPosition, myPosition, +1, 0, board);

            if (pawnFirstTurnWhite(myPosition, board)) {
                pawnMoves(moves, myPosition, myPosition, +2, 0, board);
            }
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            pawnMoves(moves, myPosition, myPosition, -1, 0, board);

            if (pawnFirstTurnBlack(myPosition, board)) {
                pawnMoves(moves, myPosition, myPosition, -2, 0, board);
            }
        }
        return moves;
    }

    boolean pawnFirstTurnWhite(ChessPosition pos, ChessBoard board) {
        var piece = board.getPiece(pos);

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return piece.getTeamColor() == ChessGame.TeamColor.WHITE && pos.getRow() == 2;
        }
        return false;
    }

    boolean pawnFirstTurnBlack(ChessPosition pos, ChessBoard board) {
        var piece = board.getPiece(pos);

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return piece.getTeamColor() == ChessGame.TeamColor.BLACK && pos.getRow() == 7;
        }
        return false;
    }

    void pawnMoves(Collection<ChessMove> moves, ChessPosition originalPos, ChessPosition pos, int rowDir, int colDir, ChessBoard board) {
        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);

        if (notRealSquare(newPos)) {
            return;
        }
        if (isEnemy(originalPos, newPos, board)) {
            moves.add(new ChessMove(originalPos, newPos, null));
        }
        else if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(originalPos, newPos, null));
        }
    }
}

//first time pawn is moved, white is at row 2, black is at row 7


/*  Pawns normally may move forward one square if that square is unoccupied,
though if it is the first time that pawn is being moved,
it may be moved forward 2 squares (provided both squares are unoccupied).
Pawns cannot capture forward, but instead capture forward diagonally
(1 square forward and 1 square sideways). They may only move diagonally like this
 if capturing an enemy piece. When a pawn moves to the end of the board
 (row 8 for white and row 1 for black), they get promoted and are replaced with
 the player's choice of Rook, Knight, Bishop, or Queen (they cannot stay a Pawn or become King).
 */