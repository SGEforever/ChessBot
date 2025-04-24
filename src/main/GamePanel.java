package main;

import Bots.V1Black;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {

    String standard_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    private final Board board;
    private final MouseHandler mouseHandler;

    private int highlightedCol = -1;
    private int highlightedRow = -1;

    private int[][] possibleSquares = new int[8][8];

    public GamePanel(Board board) {

        this.board = board;
        this.mouseHandler = new MouseHandler(this, board);

        this.setPreferredSize(new Dimension(1024, 1024));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(false);
        this.setFocusable(true);

        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawCompleteBoard(g2d);

        Piece selectedPiece = mouseHandler.getDraggedPiece();
        int dragX = mouseHandler.getDragX() - 64;
        int dragY = mouseHandler.getDragY() - 64;

        if (selectedPiece != null) {
            try {
                String imageDir = selectedPiece.getPieceImage();
                BufferedImage pieceImage = ImageIO.read(getClass().getResourceAsStream(imageDir));

                g2d.drawImage(pieceImage, dragX, dragY, 128, 128, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (highlightedCol != -1 && highlightedRow != -1) {
            g2d.setColor(new Color(255, 255, 0, 128));
            g2d.fillRect(highlightedCol * 128, highlightedRow * 128, 128, 128);
        }


        if (!Arrays.stream(possibleSquares).flatMapToInt(Arrays::stream).allMatch(value -> value == 0)) {

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (possibleSquares[col][row] == 1) {
                        g2d.setColor(new Color(0, 0, 0, 69));
                        g2d.fillOval(col * 128 + 48, row * 128 + 48, 32, 32);
                    } else if (possibleSquares[col][row] == 2) {
                        g2d.setColor(new Color(255, 0, 0, 124));
                        g2d.fillOval(col * 128 + 48, row * 128 + 48, 32, 32);
                    }
                }
            }
        }
    }

    private void drawCompleteBoard(Graphics2D g2d) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                drawField(g2d, col, row);
            }
        }
    }

    private void drawField(Graphics2D g2d, int col, int row) {
        if (col < 0 || col >= 8 || row < 0 || row >= 8) return;

        if ((row + col) % 2 == 0) {
            g2d.setColor(new Color(234, 235, 206));
        } else {
            g2d.setColor(new Color(117, 148, 84));
        }
        g2d.fillRect(col * 128, row * 128, 128, 128);

        Piece piece = board.getPieceAtPosition(col, row);
        if (piece != null) {
            try {
                String imageDir = piece.getPieceImage();
                BufferedImage pieceImage = ImageIO.read(getClass().getResourceAsStream(imageDir));
                g2d.drawImage(pieceImage, col * 128, row * 128, 128, 128, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void highlightOldField(int col, int row) {
        this.highlightedCol = col;
        this.highlightedRow = row;
        repaint(col * 128, row * 128, 128, 128);
    }

    public void drawPossibleSquares(Board board, Piece piece, int col, int row) {
        if (piece != null) {
            List<Move> possibleMoves = piece.getValidMoves(board, col, row);

            for (Move possibleMove : possibleMoves) {
                if (possibleMove.getCapturedPiece() == null) {
                    this.possibleSquares[possibleMove.getEndCol()][possibleMove.getEndRow()] = 1;
                } else {
                    this.possibleSquares[possibleMove.getEndCol()][possibleMove.getEndRow()] = 2;
                }
            }
            repaint();
        }
    }

    public void setPossibleSquaresNull() {
        this.possibleSquares = new int[8][8];
    }

    public void drawSquareWithoutPiece(int col, int row) {
        board.removePieceAtPosition(col, row);
        repaint(col*128, row*128, 128, 128);
    }
}
