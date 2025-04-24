package Bots;

import main.Board;
import main.Move;
import main.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class V1Black {

    Board board;

    boolean isActive = true;

    public V1Black(Board board) {
        this.board = board;
    }

    public void makeRandomMove(Board board) {
        List<Move> allMoves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPieceAtPosition(col, row) != null) {
                    Piece piece = board.getPieceAtPosition(col, row);
                    if (!piece.isWhite()) {
                        allMoves.addAll(piece.getValidMoves(board, col, row));
                    }
                }

            }
        }
        Random random = new Random();
        int index = random.nextInt(allMoves.size());
        board.makeMove(allMoves.get(index));
    }

    public boolean botIsActive() {
        return isActive;
    }
}
