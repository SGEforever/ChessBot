package pieces;

import main.Board;
import main.Move;
import main.Piece;

import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getValidMoves(Board board, int col, int row) {

        if (protectsKingHorizontally(board, col, row) || protectsKingVertically(board, col, row) || protectsKingDiagonally(board, col, row)) {
            return getMovesBetweenKingAndDanger(board, col, row);
        }

        int[][] directions = {
                {1, 0}, {-1, 0},
                {0, 1}, {0, -1},
                {1, 1}, {-1, -1},
                {1, -1}, {-1, 1}
        };

        return getMovesForDirection(board, col, row, directions);
    }

    @Override
    public String getPieceImage() {
        return isWhite ? "/pieces/white-queen.png" : "/pieces/black-queen.png";
    }
}
