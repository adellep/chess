package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves extends CommonMoveRules {
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        calcManyMoves(moves, myPosition, myPosition, +1, 0, board);
        calcManyMoves(moves, myPosition, myPosition, 0, +1, board);
        calcManyMoves(moves, myPosition, myPosition, -1, 0, board);
        calcManyMoves(moves, myPosition, myPosition, 0, -1, board);

        return moves;
    }
}
