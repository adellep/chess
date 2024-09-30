package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoves extends CommonRuleMoves{


    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new ArrayList<ChessMove>();

        calculateMoves(moves, pos, pos, +1, 0, board);
        calculateMoves(moves, pos, pos, 0, +1, board);
        calculateMoves(moves, pos, pos, -1, 0, board);
        calculateMoves(moves, pos, pos, 0, -1, board);

        return moves;
    }
}

