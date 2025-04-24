package main;

import javax.swing.*;
import java.awt.*;

public class Move extends JPanel {

    private Board board;

    private int startCol, startRow, endCol, endRow;
    private final Piece movedPiece, capturedPiece;

    public Move(int startCol, int startRow, int endCol, int endRow, Piece movedPiece, Piece capturedPiece) {
        this.startCol = startCol;
        this.startRow = startRow;
        this.endCol = endCol;
        this.endRow = endRow;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndCol() {
        return endCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }
}
