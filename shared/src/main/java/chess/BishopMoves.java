package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoves extends CommonRuleMoves{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new ArrayList<ChessMove>();

        calculateMoves(moves, pos, pos, +1, -1, board);
        calculateMoves(moves, pos, pos, +1, +1, board);
        calculateMoves(moves, pos, pos, -1, +1, board);
        calculateMoves(moves, pos, pos, -1, -1, board);

        return moves;
    }


    //I made BishopMoves an object, should make a superclass or interface later to not have code repeat so often

}
