package pieces;

import main.Board;
import main.Move;
import main.Piece;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getValidMoves(Board board, int col, int row) {

        List<Move> moves = new ArrayList<>();

        if (protectsKingHorizontally(board, col, row) || protectsKingVertically(board, col, row) || protectsKingDiagonally(board, col, row)) {
            return moves;
        }

        int kingCol = getTeamKingPos(board, col, row)[0];
        int kingRow = getTeamKingPos(board, col, row)[1];

        if (timesThreatened(board, kingCol, kingRow).getKey() > 1) {
            return moves;
        }

        int[][] jumps = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : jumps) {
            int endCol = col + move[0];
            int endRow = row + move[1];

            Piece targetPiece = board.getPieceAtPosition(endCol, endRow);

            if (targetPiece == null || targetPiece.isWhite() != this.isWhite()) {
                if (isInBoard(endCol, endRow)) {
                    Move possibleMove = new Move(col, row, endCol, endRow, this, targetPiece);
                    moves.add(possibleMove);
                }
            }
        }

        if (timesThreatened(board, kingCol, kingRow).getKey() == 1) {

            List<Move> blockChess = new ArrayList<>();

            int dangerCol = timesThreatened(board, kingCol, kingRow).getValue().get(0)[0];
            int dangerRow = timesThreatened(board, kingCol, kingRow).getValue().get(0)[1];
            List<Move> betweenKingAndDanger = getLineToBlock(board, dangerCol, dangerRow);

            for (Move move : moves) {
                for (Move moveBetweenKingAndDanger : betweenKingAndDanger) {
                    if (move.getEndCol() == moveBetweenKingAndDanger.getEndCol() && move.getEndRow() == moveBetweenKingAndDanger.getEndRow()) {
                        blockChess.add(move);
                    }
                }
            }
            return blockChess;
        }

        return moves;
    }

    @Override
    public String getPieceImage() {
        return isWhite ? "/pieces/white-knight.png" : "/pieces/black-knight.png";
    }
}
