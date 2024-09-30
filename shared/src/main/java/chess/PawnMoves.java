package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoves extends CommonRuleMoves{


    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new ArrayList<ChessMove>();
        var piece = board.getPiece(pos);

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                getPawnMoves(moves, pos, pos, +1, -1, board);
                getPawnMoves(moves, pos, pos, +1, 0, board);
                getPawnMoves(moves, pos, pos, +1, +1, board);

                if (firstTurnWhite(pos, board) && freeSquare(pos, +1, board) && freeSquare(pos, +2, board)) {
                    getPawnMoves(moves, pos, pos, +2, 0, board);
                }
            }
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                getPawnMoves(moves, pos, pos, -1, -1, board);
                getPawnMoves(moves, pos, pos, -1, 0, board);
                getPawnMoves(moves, pos, pos, -1, +1, board);

                if (firstTurnBlack(pos, board) && freeSquare(pos, -1, board) && freeSquare(pos, -2, board)) {
                    getPawnMoves(moves, pos, pos, -2, 0, board);
                }
            }
        }

        return moves;
    }

    boolean firstTurnWhite(ChessPosition pos, ChessBoard board) {
        ChessPiece pieceOrigPos = board.getPiece(pos);

        if (pieceOrigPos.getPieceType() == ChessPiece.PieceType.PAWN) {
            return pos.getRow() == 2;
        }
        return false;
    }

    boolean firstTurnBlack(ChessPosition pos, ChessBoard board) {
        ChessPiece pieceOrigPos = board.getPiece(pos);

        if (pieceOrigPos.getPieceType() == ChessPiece.PieceType.PAWN) {
            return pos.getRow() == 7;
        }
        return false;
    }

    boolean freeSquare(ChessPosition pos, int rowDir, ChessBoard board) {
        var oneSqFor = new ChessPosition(pos.getRow() + rowDir, pos.getColumn());
        var twoSqFor = new ChessPosition(oneSqFor.getRow() + rowDir, oneSqFor.getColumn());

        return board.getPiece(oneSqFor) == null && board.getPiece(twoSqFor) == null;
    }

    void promotePawn(Collection<ChessMove> moves, ChessPosition origPos, ChessPosition newPos) {
        if (newPos.getRow() == 1 || newPos.getRow() == 8) {
            moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.QUEEN));
        }
        else {
            moves.add(new ChessMove(origPos, newPos, null));
        }
    }

    void getPawnMoves(Collection<ChessMove> moves, ChessPosition origPos, ChessPosition pos, int rowDir, int colDir, ChessBoard board) {
        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);

        if (!isRealSquare(newPos)) {
            return;
        }
        if (colDir == 0) {
            if (board.getPiece(newPos) == null) {
                promotePawn(moves, origPos, newPos);
            }
        }
        else if (isEnemy(origPos, newPos, board)) {
            promotePawn(moves, origPos, newPos);
        }
    }
}