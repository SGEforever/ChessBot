package main;

import java.util.ArrayList;
import java.util.List;

public class GameLog {

    private final List<Move> moves = new ArrayList<>();

    public GameLog() {
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Move getLastMove() {
        if (moves.isEmpty()) {
            return null;
        }
        return moves.get(moves.size() - 1);
    }

    public void addMove(Move move) {
        this.moves.add(move);
    }

    public void printMoves() {
        for (Move move : moves) {
            System.out.println(move);
        }
    }
}
