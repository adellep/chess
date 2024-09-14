package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    //make bishopcalculator method static*** to not have to make a new object
    //I made BishopMoves an object, could add a superclass later to not have code repeat so often

    public  Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        var piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            getBishopMove(moves, myPosition, myPosition, +1, -1, true, board);
            getBishopMove(moves, myPosition, myPosition, +1, +1, true, board);
            getBishopMove(moves, myPosition, myPosition, -1, -1, true, board);
            getBishopMove(moves, myPosition, myPosition, -1, +1, true, board);
        }

        return moves;
    }

    //new method: check if bishop move is actually moving to a real square (bool)
    boolean isRealSquare(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();

        return row >= 0 && row <= 8 && col >= 0 && col <= 8;
    }

    //if piece is in square and friendly
    //if piece is in square and enemy
    boolean isEnemy(ChessPosition origPos, ChessPosition newPos, ChessBoard board) {
        ChessPiece pieceOrigPos = board.getPiece(origPos);
        ChessPiece pieceNewPos = board.getPiece(newPos);

        if(pieceNewPos == null) {
            return false;
        }

        return pieceNewPos.getTeamColor() != pieceOrigPos.getTeamColor(); //if return true there is an enemy in square
    }


    void getBishopMove(Collection<ChessMove> moves, ChessPosition origPos,
                                     ChessPosition pos, int rowDir, int colDir, boolean keepGoing, ChessBoard board) {
        if (isRealSquare(pos)) { //if square exists on board
            var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);

            if(isEnemy(origPos, newPos, board)) {
                moves.add(new ChessMove(origPos, newPos, null)); //eat enemy and must stop
                keepGoing = false;
            } else if (board.getPiece(newPos) == null) { //no piece so add position
                moves.add(new ChessMove(origPos, newPos, null));
            } else {
                keepGoing = false; //stop if there is team color piece in square
            }


            if (keepGoing) { //if no piece
                getBishopMove(moves, origPos, newPos, rowDir, colDir, keepGoing, board);
            }
        }
    }
}
