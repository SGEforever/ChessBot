package main;

import pieces.Rook;

import java.awt.event.*;

public class MouseHandler extends MouseAdapter {

    private final Board board;
    private final GamePanel gamePanel;

    private Piece selectedPiece;
    private int startCol, startRow;
    private int dragX, dragY;

    public MouseHandler(GamePanel gamePanel, Board board) {
        this.gamePanel = gamePanel;
        this.board = board;
    }

    public Piece getDraggedPiece() {
        return selectedPiece;
    }

    public int getDragX() {
        return dragX;
    }

    public int getDragY() {
        return dragY;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        startCol = e.getX() / 128;
        startRow = e.getY() / 128;
        selectedPiece = board.getPieceAtPosition(startCol, startRow);

        gamePanel.setPossibleSquaresNull();
        gamePanel.drawPossibleSquares(board, selectedPiece, startCol, startRow);

        if (selectedPiece != null) {
            dragX = e.getX();
            dragY = e.getY();
            gamePanel.drawSquareWithoutPiece(startCol, startRow);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedPiece != null) {
            dragX = e.getX();
            dragY = e.getY();
            gamePanel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int endCol = e.getX() / 128;
        int endRow = e.getY() / 128;

        if (selectedPiece != null) {

            board.setPieceAtPosition(selectedPiece, startCol, startRow);
            gamePanel.setPossibleSquaresNull();

            if (startCol != endCol || startRow != endRow) {

                Move move = new Move(startCol, startRow, endCol, endRow, selectedPiece, board.getPieceAtPosition(endCol, endRow));
                Move validMove = board.getValidMove(move);

                if (validMove != null && (board.isPlayerToMove() == selectedPiece.isWhite)) {
                    board.makeMove(validMove);



                    gamePanel.repaint();
                    gamePanel.highlightOldField(startCol, startRow);
                } else {
                    board.cancelMove(move);
                }
            } else {
                gamePanel.repaint();
            }
        }
        selectedPiece = null;
    }
}
