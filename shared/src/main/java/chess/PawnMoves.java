package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {

    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        var piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            getPawnMove(moves, myPosition, myPosition, +1, 0, board);
            //getPawnMove(moves, myPosition, myPosition, +2, 0, board); //for 1st turn
            //getPawnMove(moves, myPosition, myPosition, +1, -1, board); //attack left diagonal
            //getPawnMove(moves, myPosition, myPosition, +1, +1, board); //attack right diagonal
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

    boolean isFirstTurn(ChessPosition pos,  ChessBoard board) {
        ChessPiece pieceOrigPos = board.getPiece(pos);

        if (pos.getRow() == 2 && pieceOrigPos.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return true;
        }
        else if (pos.getRow() == 7 && pieceOrigPos.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return true;
        }
        else {
            return false;
        }
    }

//    boolean endOfBoard(ChessPosition pos, ChessBoard board) {
//
//    }

    void getPawnMove(Collection<ChessMove> moves, ChessPosition origPos,
                  ChessPosition pos, int rowDir, int colDir, ChessBoard board) {
        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);

        if (!isRealSquare(newPos)) {
            return;
        }
//        if(isFirstTurn(origPos, board)) {
//
//        }
        if (isEnemy(origPos, newPos, board)) {
            moves.add(new ChessMove(origPos, newPos, null)); //will have to change null
        }
        else if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(origPos, newPos, null)); //will have to change null
        }
    }
}
