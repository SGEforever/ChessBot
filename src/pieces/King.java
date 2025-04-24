package pieces;

import main.Board;
import main.Move;
import main.Piece;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getValidMoves(Board board, int col, int row) {

        List<Move> moves = new ArrayList<>();

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {

                Piece pieceAtPosition = board.getPieceAtPosition(j, i);

                if (isInBoard(j, i)) {
                    if (pieceAtPosition == null || pieceAtPosition.isWhite() != isWhite) {
                        if (timesThreatened(board, j, i).getKey() == 0) {
                            Move move = new Move(col, row, j, i, this, pieceAtPosition);
                            moves.add(move);
                        }
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public String getPieceImage() {
        return isWhite ? "/pieces/white-king.png" : "/pieces/black-king.png";
    }

    private boolean isCheck(Board board, int col, int row) {
        return timesThreatened(board, col, row) .getKey()> 0;
    }

    private boolean isMate(Board board, int col, int row) {
        return false;
    }
}
