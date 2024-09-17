package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();

        var piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { //positives
                getPawnMove(moves, myPosition, myPosition, +1, 0, board);
                getPawnMove(moves, myPosition, myPosition, +1, -1, board);
                getPawnMove(moves, myPosition, myPosition, +1, +1, board);

                if (isFirstTurnWhite(myPosition, board) && !isBlocked(myPosition, +1, board)
                        && !isBlocked(myPosition, +2, board)) {
                    getPawnMove(moves, myPosition, myPosition, +2, 0, board); //for 1st turn
                }
            }
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) { //negatives
                getPawnMove(moves, myPosition, myPosition, -1, 0, board);
                getPawnMove(moves, myPosition, myPosition, -1, -1, board);
                getPawnMove(moves, myPosition, myPosition, -1, +1, board);

                if (isFirstTurnBlack(myPosition, board) && !isBlocked(myPosition, -1, board)
                        && !isBlocked(myPosition, -2, board)) {
                    getPawnMove(moves, myPosition, myPosition, -2, 0, board);
                }
            }
        }
        return moves;
    }

    boolean isRealSquare(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();

        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    boolean isEnemy(ChessPosition origPos, ChessPosition newPos, ChessBoard board) { //change to check diagonally forward squares
        ChessPiece pieceOrigPos = board.getPiece(origPos);
        ChessPiece pieceNewPos = board.getPiece(newPos);

        if (pieceNewPos == null) {
            return false;
        }

        return pieceNewPos.getTeamColor() != pieceOrigPos.getTeamColor();
    }

    boolean isFirstTurnWhite(ChessPosition pos, ChessBoard board) {
        ChessPiece pieceOrigPos = board.getPiece(pos);

        if (pieceOrigPos.getPieceType() == ChessPiece.PieceType.PAWN) {
            return pos.getRow() == 2;
        }
        return false;
    }

    boolean isFirstTurnBlack(ChessPosition pos, ChessBoard board) {
        ChessPiece pieceOrigPos = board.getPiece(pos);

        if (pieceOrigPos.getPieceType() == ChessPiece.PieceType.PAWN) {
            return pos.getRow() == 7;
        }
        return false;
    }

    boolean isBlocked(ChessPosition pos, int rowDir, ChessBoard board) {
        var oneSquareFor = new ChessPosition(pos.getRow() + rowDir, pos.getColumn());
        var twoSquaresFor = new ChessPosition(oneSquareFor.getRow() + rowDir, oneSquareFor.getColumn());

        return board.getPiece(oneSquareFor) != null || board.getPiece(twoSquaresFor) != null;
    }

    void getPawnMove(Collection<ChessMove> moves, ChessPosition origPos,
                  ChessPosition pos, int rowDir, int colDir, ChessBoard board) {
        var newPos = new ChessPosition(pos.getRow() + rowDir, pos.getColumn() + colDir);

        if (!isRealSquare(newPos)) {
            return;
        }

        ChessPiece pieceNewPos = board.getPiece(newPos);
        if (colDir == 0) { //only moving forward
            if (pieceNewPos == null) {
                if (newPos.getRow() == 8 || newPos.getRow() == 1) {
                    moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.QUEEN));
                } else {
                    moves.add(new ChessMove(origPos, newPos, null));
                }
            }
        }
        else if (isEnemy(origPos, newPos, board)) {
            if (newPos.getRow() == 8 || newPos.getRow() == 1) {
                moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(origPos, newPos, ChessPiece.PieceType.QUEEN));
            } else {
                moves.add(new ChessMove(origPos, newPos, null));
            }
        }
    }
}
