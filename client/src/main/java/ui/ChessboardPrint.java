package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessboardPrint {

    public void printBoard(ChessGame game, ChessGame.TeamColor perspective) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawHeaders(out, perspective);
        drawRows(out, game, perspective);
        drawHeaders(out, perspective);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private void drawHeaders(PrintStream out, ChessGame.TeamColor perspective) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        String[] headers;
        if (perspective == ChessGame.TeamColor.BLACK) {
            headers = new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        } else {
            headers = new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        }
        out.print(EMPTY);
        for (String header : headers) {
            out.print(header);
        }
        out.print(EMPTY);
        out.println();
    }

    private void drawRows(PrintStream out, ChessGame game, ChessGame.TeamColor perspective) {
        int startRow = (perspective == ChessGame.TeamColor.BLACK) ? 1 : 8;
        int endRow = (perspective == ChessGame.TeamColor.BLACK) ? 8 : 1;
        int rowIncrement = (perspective == ChessGame.TeamColor.BLACK) ? 1 : -1;

        for (int row = startRow; (rowIncrement > 0) ? row <= endRow : row >= endRow; row += rowIncrement) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(" %d ", row);

            for (int col = 1; col <= 8; col++) {
                int current_col = (perspective == ChessGame.TeamColor.BLACK) ? 9-col : col;

                boolean lightSquaure = (row + current_col) % 2 != 0;
                if (lightSquaure) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row, current_col));
                out.print(getPieceSymbol(piece));
            }

            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(" %d ", row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
    }

    private String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA + WHITE_KING : SET_TEXT_COLOR_GREEN + BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA + WHITE_QUEEN : SET_TEXT_COLOR_GREEN + BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA + WHITE_BISHOP : SET_TEXT_COLOR_GREEN + BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA + WHITE_KNIGHT : SET_TEXT_COLOR_GREEN + BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA + WHITE_ROOK : SET_TEXT_COLOR_GREEN + BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA + WHITE_PAWN : SET_TEXT_COLOR_GREEN + BLACK_PAWN;
        };
    }
}

