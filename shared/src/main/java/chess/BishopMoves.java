package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoves extends CommonMoveRules {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        getBishopMoves(moves, myPosition, myPosition, +1, -1, board);
        getBishopMoves(moves, myPosition, myPosition, +1, +1, board);
        getBishopMoves(moves, myPosition, myPosition, -1, +1, board);
        getBishopMoves(moves, myPosition, myPosition, -1, -1, board);

        return moves;
    }
}
