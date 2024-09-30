package chess;

import java.util.Collection;

public abstract class CommonRuleMoves implements MoveRules{

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

    protected void calculateMoves(Collection<ChessMove> moves, ChessPosition origPos, ChessPosition pos, int rowDir, int colDir, ChessBoard board) {
        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);

        if (!isRealSquare(newPos)) {
            return;
        }

        boolean keepGoing = true;
        if (isEnemy(origPos, newPos, board)) {
            moves.add(new ChessMove(origPos, newPos, null));
            keepGoing = false;
        }
        else if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(origPos, newPos, null));
        }
        else {
            keepGoing = false; //bc teammate
        }

        if (keepGoing) {
            calculateMoves(moves, origPos, newPos, rowDir, colDir, board);
        }
    }

    protected void calculateOneMove(Collection<ChessMove> moves, ChessPosition origPos, ChessPosition pos, int rowDir, int colDir, ChessBoard board) {
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
