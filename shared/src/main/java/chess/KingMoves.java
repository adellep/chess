package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves extends CommonMoveRules {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        calcOneMove(moves, myPosition, myPosition, +1, 0, board);
        calcOneMove(moves, myPosition, myPosition, 0, +1, board);
        calcOneMove(moves, myPosition, myPosition, -1, 0, board);
        calcOneMove(moves, myPosition, myPosition, 0, -1, board);
        calcOneMove(moves, myPosition, myPosition, +1, -1, board);
        calcOneMove(moves, myPosition, myPosition, +1, +1, board);
        calcOneMove(moves, myPosition, myPosition, -1, +1, board);
        calcOneMove(moves, myPosition, myPosition, -1, -1, board);

        return moves;
    }
}
