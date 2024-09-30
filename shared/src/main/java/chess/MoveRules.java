package chess;

import java.util.Collection;

public interface MoveRules {
    Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
