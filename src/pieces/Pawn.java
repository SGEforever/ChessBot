package pieces;

import main.Board;
import main.GameLog;
import main.Move;
import main.Piece;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getValidMoves(Board board, int col, int row) {

        List<Move> moves = new ArrayList<>();
        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 6 : 1;

        if ((!protectsKingHorizontally(board, col, row)) && (!protectsKingDiagonally(board, col, row))) {

            if (board.getPieceAtPosition(col, row - direction) == null) {

                if (isInBoard(col, row - direction)) {
                    Move normalMove = new Move(col, row, col, row - direction, this, board.getPieceAtPosition(col, row - direction));
                    moves.add(normalMove);
                }
            }

            if (row == startRow && board.getPieceAtPosition(col, row - 2 * direction) == null) {
                moves.add(new Move(col, row, col, row - 2 * direction, this, board.getPieceAtPosition(col, row - 2 * direction)));
            }

            if (isInBoard(col + 1, row - direction)) {
                if (board.getPieceAtPosition(col + 1, row - direction) != null) {
                    if (board.getPieceAtPosition(col + 1, row - direction).isWhite() != isWhite) {
                        moves.add(new Move(col, row, col + 1, row - direction, this, board.getPieceAtPosition(col + 1, row - direction)));
                    }
                }
            }
            if (isInBoard(col - 1, row - direction)) {
                if (board.getPieceAtPosition(col - 1, row - direction) != null) {
                    if (board.getPieceAtPosition(col - 1, row - direction).isWhite() != isWhite) {
                        moves.add(new Move(col, row, col - 1, row - direction, this, board.getPieceAtPosition(col - 1, row - direction)));
                    }
                }
            }

            GameLog gameLog = board.getGameLog();

            if (gameLog.getLastMove() != null) {

                Move lastMove = gameLog.getLastMove();

                if (lastMove.getMovedPiece() instanceof Pawn && Math.abs(lastMove.getStartRow() - lastMove.getEndRow()) == 2) {
                    if (row == lastMove.getEndRow() && col - 1 == lastMove.getEndCol()) {
                        moves.add(new Move(col, row, col - 1, row - direction, this, board.getPieceAtPosition(col - 1, row)));
                    }
                    if (row == lastMove.getEndRow() && col + 1 == lastMove.getEndCol()) {
                        moves.add(new Move(col, row, col + 1, row - direction, this, board.getPieceAtPosition(col + 1, row)));
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public String getPieceImage() {
        return isWhite ? "/pieces/white-pawn.png" : "/pieces/black-pawn.png";
    }
}
