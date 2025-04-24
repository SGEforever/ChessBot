package main;

import Bots.V1Black;
import pieces.*;
import java.util.HashMap;

public class Board {

    private boolean whiteToMove = true;

    private V1Black bot;

    private final Piece[][] pieces;
    private final GameLog gameLog;
    private GamePanel gamePanel;
    private String fen;

    public Board(String fen) {
        this.pieces = new Piece[8][8];
        this.gamePanel = new GamePanel(this);
        this.gameLog = new GameLog();
        this.bot = new V1Black(this);
        loadBoardFromFen(this, fen);
    }

    private void loadBoardFromFen(Board board, String fen) {

        HashMap<Character, Piece> pieceTypeFromSymbol = new HashMap<>();
        pieceTypeFromSymbol.put('k', new King(false));
        pieceTypeFromSymbol.put('q', new Queen(false));
        pieceTypeFromSymbol.put('r', new Rook(false));
        pieceTypeFromSymbol.put('b', new Bishop(false));
        pieceTypeFromSymbol.put('n', new Knight(false));
        pieceTypeFromSymbol.put('p', new Pawn(false));
        pieceTypeFromSymbol.put('K', new King(true));
        pieceTypeFromSymbol.put('Q', new Queen(true));
        pieceTypeFromSymbol.put('R', new Rook(true));
        pieceTypeFromSymbol.put('B', new Bishop(true));
        pieceTypeFromSymbol.put('N', new Knight(true));
        pieceTypeFromSymbol.put('P', new Pawn(true));

        int row = 0;
        int col = 0;

        for(char c : fen.toCharArray()) {

            if (c == '/') {
                row++;
                col = 0;
            }

            if (Character.isDigit(c)) {

                int value = Character.getNumericValue(c);

                for (int i = col; i < col + value; i++) {
                    board.pieces[i][row] = null;
                }
                col += value;
            }

            if (Character.isLetter(c)) {
                Piece piece = pieceTypeFromSymbol.get(c);
                board.pieces[col][row] = piece;
                col++;
            }
        }
    }

    public Piece getPieceAtPosition(int col, int row) {
        if (col >= 0 && col < 8 && row >= 0 && row < 8) {
            return pieces[col][row];
        }
        return null;
    }

    public void removePieceAtPosition(int col, int row) {
        pieces[col][row] = null;
    }

    public void setPieceAtPosition(Piece piece, int col, int row) {
        pieces[col][row] = piece;
    }

    public void makeMove(Move move) {
        if (move.getMovedPiece() != null) {

            if ((move.getMovedPiece() instanceof Pawn) && (move.getCapturedPiece() instanceof Pawn)) {
                if ((move.getEndCol() < move.getStartCol()) && (this.getPieceAtPosition(move.getEndCol(), move.getEndRow()) == null)) {
                    pieces[move.getEndCol()][move.getStartRow()] = null;
                }
                if ((move.getEndCol() > move.getStartCol()) && (this.getPieceAtPosition(move.getEndCol(), move.getEndRow()) == null)) {
                    pieces[move.getEndCol()][move.getStartRow()] = null;
                }
            }

            pieces[move.getStartCol()][move.getStartRow()] = null;
            pieces[move.getEndCol()][move.getEndRow()] = move.getMovedPiece();
            gameLog.addMove(move);

            if (gamePanel != null) {
                int startX = move.getStartCol() * 128;
                int startY = move.getStartRow() * 128;
                int endX = move.getEndCol() * 128;
                int endY = move.getEndRow() * 128;

                gamePanel.repaint(startX, startY, 128, 128);
                gamePanel.repaint(endX, endY, 128, 128);
            }

            changePlayerToMove();

            if (!isPlayerToMove()) {
                if (bot.botIsActive()) {
                    bot.makeRandomMove(this);
                }
            }
        }
    }

    public void cancelMove(Move move) {
        if (move.getMovedPiece() != null) {
            pieces[move.getStartCol()][move.getStartRow()] = move.getMovedPiece();

            if (gamePanel != null) {
                gamePanel.repaint();
            }
        }
    }

    public Move getValidMove(Move move) {
        for (Move m : move.getMovedPiece().getValidMoves(this, move.getStartCol(), move.getStartRow())) {
            if (move.getEndRow() == m.getEndRow() && move.getEndCol() == m.getEndCol()) {
                return m;
            }
        }
        return null;
    }

    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.println(pieces[col][row]);
            }
        }
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public GameLog getGameLog() {
        return gameLog;
    }

    public boolean isPlayerToMove() {
        return whiteToMove;
    }

    public void changePlayerToMove() {
        this.whiteToMove = !whiteToMove;
    }
}
