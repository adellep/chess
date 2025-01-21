package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMoves extends CommonMoveRules {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        calcOneMove(moves, myPosition, myPosition, +1, -2, board);
        calcOneMove(moves, myPosition, myPosition, +2, -1, board);
        calcOneMove(moves, myPosition, myPosition, +2, +1, board);
        calcOneMove(moves, myPosition, myPosition, +1, +2, board);
        calcOneMove(moves, myPosition, myPosition, -1, +2, board);
        calcOneMove(moves, myPosition, myPosition, -2, +1, board);
        calcOneMove(moves, myPosition, myPosition, -2, -1, board);
        calcOneMove(moves, myPosition, myPosition, -1, -2, board);

        return moves;
    }
}
