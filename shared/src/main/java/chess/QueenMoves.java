package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        var piece = board.getPiece(myPosition);
        if(piece.getPieceType() == ChessPiece.PieceType.QUEEN) { //I CAN SIMPLIFY THIS WITH A SUPERCLASS i think
            getQueenMove(moves, myPosition, myPosition, +1, -1, board);
            getQueenMove(moves, myPosition, myPosition, +1, +1, board);
            getQueenMove(moves, myPosition, myPosition, -1, +1, board);
            getQueenMove(moves, myPosition, myPosition, -1, -1, board);

            getQueenMove(moves, myPosition, myPosition, +1, 0, board);
            getQueenMove(moves, myPosition, myPosition, 0, +1, board);
            getQueenMove(moves, myPosition, myPosition, -1, 0, board);
            getQueenMove(moves, myPosition, myPosition, 0, -1, board);
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

        if(pieceNewPos == null) {
            return false;
        }

        return pieceNewPos.getTeamColor() != pieceOrigPos.getTeamColor(); //if return true there is an enemy in square
    }


    void getQueenMove(Collection<ChessMove> moves, ChessPosition origPos,
                       ChessPosition pos, int rowDir, int colDir, ChessBoard board) {
        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);
        if (!isRealSquare(newPos)) {
            return;
        }

        boolean keepGoing = true;
        if(isEnemy(origPos, newPos, board)) {
            moves.add(new ChessMove(origPos, newPos, null));
            keepGoing = false;
        } else if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(origPos, newPos, null));
        } else {
            keepGoing = false;
        }


        if (keepGoing) {
            getQueenMove(moves, origPos, newPos, rowDir, colDir, board);
        }
    }
}
