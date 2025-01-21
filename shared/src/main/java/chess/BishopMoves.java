package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoves extends CommonMoveRules {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        calcManyMoves(moves, myPosition, myPosition, +1, -1, board);
        calcManyMoves(moves, myPosition, myPosition, +1, +1, board);
        calcManyMoves(moves, myPosition, myPosition, -1, +1, board);
        calcManyMoves(moves, myPosition, myPosition, -1, -1, board);

        return moves;
    }
}
