package pieces;

import main.Board;
import main.Move;
import main.Piece;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getValidMoves(Board board, int col, int row) {

        if (protectsKingHorizontally(board, col, row) || protectsKingVertically(board, col, row)) {
            return new ArrayList<>();
        }

        if (protectsKingDiagonally(board, col, row)) {
            return getMovesBetweenKingAndDanger(board, col, row);
        }

        int[][] directions = {
                {1, 1}, {-1, -1},
                {1, -1}, {-1, 1}
        };

        return getMovesForDirection(board, col, row, directions);
    }

    @Override
    public String getPieceImage() {
        return isWhite ? "/pieces/white-bishop.png" : "/pieces/black-bishop.png";
    }
}
