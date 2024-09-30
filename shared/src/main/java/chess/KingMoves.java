package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMoves extends CommonRuleMoves{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new ArrayList<ChessMove>();

        calculateOneMove(moves, pos, pos, +1, -1, board);
        calculateOneMove(moves, pos, pos, +1, +1, board);
        calculateOneMove(moves, pos, pos, -1, +1, board);
        calculateOneMove(moves, pos, pos, -1, -1, board);

        calculateOneMove(moves, pos, pos, +1, 0, board);
        calculateOneMove(moves, pos, pos, 0, +1, board);
        calculateOneMove(moves, pos, pos, -1, 0, board);
        calculateOneMove(moves, pos, pos, 0, -1, board);

        return moves;
    }
}
