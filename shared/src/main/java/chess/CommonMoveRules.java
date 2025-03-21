package chess;

import java.util.Collection;

public abstract class CommonMoveRules implements MoveRules {

    boolean notRealSquare(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();

        return !(row >= 1 && row <= 8 && col >= 1 && col <= 8);
    }

    boolean isEnemy(ChessPosition originalPos, ChessPosition newPos, ChessBoard board) {
        var pieceOriginalPos = board.getPiece(originalPos);
        var pieceNewPos = board.getPiece(newPos);

        if (pieceNewPos == null) {
            return false;
        }

        return pieceOriginalPos.getTeamColor() != pieceNewPos.getTeamColor();
    }

    void calcManyMoves(Collection<ChessMove> moves, ChessPosition originalPos, ChessPosition pos, int rowDir, int colDir, ChessBoard board) {

        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);

        if(notRealSquare(newPos)) {
            return;
        }

        boolean keepMoving = true;
        if (isEnemy(originalPos, newPos, board)) {
            moves.add(new ChessMove(originalPos, newPos, null));
            keepMoving = false;
        }
        else if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(originalPos, newPos, null));
        }
        else {
            keepMoving = false;
        }

        if (keepMoving) {
            calcManyMoves(moves, originalPos, newPos, rowDir, colDir, board);
        }
    }

    void calcOneMove(Collection<ChessMove> moves, ChessPosition originalPos, ChessPosition pos, int rowDir, int colDir, ChessBoard board) {
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
