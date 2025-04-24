package pieces;

import main.Board;
import main.Move;
import main.Piece;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getValidMoves(Board board, int col, int row) {

        if (protectsKingDiagonally(board, col, row)) {
            return new ArrayList<>();
        }

        if (protectsKingHorizontally(board, col, row) || protectsKingVertically(board, col, row)) {
            return getMovesBetweenKingAndDanger(board, col, row);
        }

        int[][] directions = {
                {1, 0}, {-1, 0},
                {0, 1}, {0, -1}
        };

        return getMovesForDirection(board, col, row, directions);
    }

    @Override
    public String getPieceImage() {
        return isWhite ? "/pieces/white-rook.png" : "/pieces/black-rook.png";
    }
}
