package main;

import pieces.*;

import java.util.*;

public abstract class Piece {

    protected boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract List<Move> getValidMoves(Board board, int col, int row);

    public abstract String getPieceImage();

    public boolean moveIsInBoard(Move move) {
        return move.getEndCol() >= 0 && move.getEndCol() <= 7 && move.getEndRow() >= 0 && move.getEndRow() <= 7;
    }

    public boolean isInBoard(int col, int row) {
        return col >= 0 && col <= 7 && row >= 0 && row <= 7;
    }

    public List<Move> getMovesForDirection(Board board, int startCol, int startRow, int[][] directions) {

        List<Move> moves = new ArrayList<>();
        int kingCol = getTeamKingPos(board, startCol, startRow)[0];
        int kingRow = getTeamKingPos(board, startCol, startRow)[1];

        if (timesThreatened(board, kingCol, kingRow).getKey() > 1) {
            return moves;
        }

        for (int[] dir : directions) {
            int col = startCol;
            int row = startRow;

            while (true) {
                col += dir[0];
                row += dir[1];

                if (!isInBoard(col, row)) {
                    break;
                }

                Piece targetPiece = board.getPieceAtPosition(col, row);

                if (targetPiece == null) {
                    moves.add(new Move(startCol, startRow, col, row, this, board.getPieceAtPosition(col, row)));
                } else {
                    if (targetPiece.isWhite() != this.isWhite()) {
                        moves.add(new Move(startCol, startRow, col, row, this, board.getPieceAtPosition(col, row)));
                    }
                    break;
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

    public boolean protectsKingHorizontally(Board board, int col, int row) {

        int[][] directions = {
                {1, 0}, {-1, 0}
        };

        return dangerForKingInDirection(board, col, row, directions);
    }

    public boolean protectsKingVertically(Board board, int col, int row) {

        int[][] directions = {
                {0, 1}, {0, -1}
        };

        return dangerForKingInDirection(board, col, row, directions);
    }

    public boolean protectsKingDiagonally(Board board, int col, int row) {

        int[][] directions = {
                {1, 1}, {-1, -1},
                {1, -1}, {-1, 1}
        };

        return dangerForKingInDirection(board, col, row, directions);
    }

    public boolean dangerForKingInDirection(Board board, int startCol, int startRow, int[][] directions) {

        int[] kingPos = getTeamKingPos(board, startCol, startRow);
        int kingCol = kingPos[0];
        int kingRow = kingPos[1];

        for (int[] dir : directions) {
            int col = kingCol;
            int row = kingRow;

            if (dir[0] == 0 && (kingCol != startCol)) {
                continue;
            }
            if (dir[1] == 0 && (kingRow != startRow)) {
                continue;
            }

            int pieceColDir = startCol > kingCol ? 1 : -1;
            int pieceRowDir = startRow > kingRow ? 1 : -1;

            if ((dir[0] != 0) && (dir[1] != 0)) {

                if (Math.abs(kingCol - startCol) != Math.abs(kingRow - startRow)) {
                    continue;
                } else {

                    if (pieceColDir != dir[0] || pieceRowDir != dir[1]) {
                        continue;
                    }
                }
            }

            if ((dir[1] == 0) && (dir[0] != pieceColDir)) {
                continue;
            }

            if ((dir[0] == 0) && (dir[1] != pieceRowDir)) {
                continue;
            }

            while (true) {
                col += dir[0];
                row += dir[1];

                if (!isInBoard(col, row)) {
                    break;
                }

                Piece piece = board.getPieceAtPosition(col, row);

                if (piece != null) {

                    if ((col != kingCol || row != kingRow) && (col != startCol || row != startRow)) {

                        if (piece.isWhite != board.getPieceAtPosition(startCol, startRow).isWhite) {

                            if ((dir[0] == 0) || (dir[1] == 0)) {
                                return piece instanceof Queen || piece instanceof Rook;
                            } else {
                                return piece instanceof Queen || piece instanceof Bishop;
                            }

                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<Move> getMovesBetweenKingAndDanger(Board board, int startCol, int startRow) {

        List<Move> moves = new ArrayList<>();

        int kingCol = getTeamKingPos(board, startCol, startRow)[0];
        int kingRow = getTeamKingPos(board, startCol, startRow)[1];

        int xDir = Integer.compare(startCol, kingCol);
        int yDir = Integer.compare(startRow, kingRow);

        while (true) {
            kingCol += xDir;
            kingRow += yDir;

            if (kingCol == startCol && kingRow == startRow) {
                continue;
            }

            moves.add(new Move(startCol, startRow, kingCol, kingRow, this, board.getPieceAtPosition(kingCol, kingRow)));

            if (board.getPieceAtPosition(kingCol, kingRow) != null) {
                break;
            }
        }

        return moves;
    }

    public List<Move> getLineToBlock(Board board, int dangerCol, int dangerRow) {
        List<Move> moves = new ArrayList<>();

        int kingCol = getOppositeKingPos(board, dangerCol, dangerRow)[0];
        int kingRow = getOppositeKingPos(board, dangerCol, dangerRow)[1];

        int xDir = Integer.compare(dangerCol, kingCol);
        int yDir = Integer.compare(dangerRow, kingRow);

        do {
            kingCol += xDir;
            kingRow += yDir;

            moves.add(new Move(dangerCol, dangerRow, kingCol, kingRow, this, board.getPieceAtPosition(kingCol, kingRow)));

        } while (board.getPieceAtPosition(kingCol, kingRow) == null);

        return moves;
    }

    public int[] getTeamKingPos(Board board, int col, int row) {
        int[] kingPos = new int[2];

        for (int kingRow = 0; kingRow < 8; kingRow++) {
            for (int kingCol = 0; kingCol < 8; kingCol++) {

                if (board.getPieceAtPosition(kingCol, kingRow) != null) {

                    if (board.getPieceAtPosition(kingCol, kingRow) instanceof King && (board.getPieceAtPosition(kingCol, kingRow).isWhite == board.getPieceAtPosition(col, row).isWhite)) {

                        kingPos[0] = kingCol;
                        kingPos[1] = kingRow;
                    }
                }
            }
        }
        return kingPos;
    }

    public int[] getOppositeKingPos(Board board, int col, int row) {
        int[] kingPos = new int[2];

        for (int kingRow = 0; kingRow < 8; kingRow++) {
            for (int kingCol = 0; kingCol < 8; kingCol++) {

                if (board.getPieceAtPosition(kingCol, kingRow) != null) {

                    if (board.getPieceAtPosition(kingCol, kingRow) instanceof King && (board.getPieceAtPosition(kingCol, kingRow).isWhite != board.getPieceAtPosition(col, row).isWhite)) {

                        kingPos[0] = kingCol;
                        kingPos[1] = kingRow;
                    }
                }
            }
        }
        return kingPos;
    }

    public Map.Entry<Integer, List<int[]>> timesThreatened(Board board, int startCol, int startRow) {

        int[][] directions = {
                {1, 0}, {-1, 0},
                {0, 1}, {0, -1},
                {1, 1}, {-1, -1},
                {1, -1}, {-1, 1}
        };

        int threatened = 0;
        List<int[]> threatenedfromPiece = new ArrayList<>();

        for (int[] dir : directions) {

            int xDir = dir[0];
            int yDir = dir[1];
            int col = startCol;
            int row = startRow;

            while (true) {

                col += xDir;
                row += yDir;

                if (!isInBoard(col, row)) {
                    break;
                }

                if (board.getPieceAtPosition(col, row) != null) {
                    Piece possibleThreat = board.getPieceAtPosition(col, row);
                    if (this.isWhite != possibleThreat.isWhite) {
                        if (xDir == 0 || yDir == 0) {
                            if (possibleThreat instanceof Queen || possibleThreat instanceof Rook) {
                                threatened++;
                                threatenedfromPiece.add(new int[]{col, row});
                            }
                        } else {
                            if (possibleThreat instanceof Queen || possibleThreat instanceof Bishop) {
                                threatened++;
                                threatenedfromPiece.add(new int[]{col, row});
                            }
                        }
                    }
                    break;
                }
            }
        }

        int[][] jumps = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] jump : jumps) {
            int xJump = jump[0];
            int yJump = jump[1];

            if (isInBoard(startCol + xJump, startRow + yJump)) {
                if (board.getPieceAtPosition(startCol + xJump, startRow + yJump) != null) {
                    Piece possibleThreat = board.getPieceAtPosition(startCol + xJump, startRow + yJump);
                    if (possibleThreat.isWhite != this.isWhite && possibleThreat instanceof Knight) {
                        threatened++;
                        threatenedfromPiece.add(new int[]{startCol + xJump, startRow + yJump});
                    }
                }
            }
        }

        int direction = this.isWhite ? 1 : -1;

        if (isInBoard(startCol - 1, startRow - direction)) {
            if (board.getPieceAtPosition(startCol - 1, startRow - direction) instanceof Pawn) {
                if (this.isWhite != board.getPieceAtPosition(startCol - 1, startRow - direction).isWhite) {
                    threatened++;
                    threatenedfromPiece.add(new int[]{startCol - 1, startRow - direction});
                }
            }
        }
        if (isInBoard(startCol + 1, startRow - direction)) {
            if (board.getPieceAtPosition(startCol + 1, startRow - direction) instanceof Pawn) {
                if (this.isWhite != board.getPieceAtPosition(startCol + 1, startRow - direction).isWhite) {
                    threatened++;
                    threatenedfromPiece.add(new int[]{startCol + 1, startRow - direction});
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(threatened, threatenedfromPiece);
    }
}
