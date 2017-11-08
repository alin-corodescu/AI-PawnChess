package fii.ai.pawnchess.core;

import javafx.geometry.Pos;

import java.util.*;

/**
 * Holds information about the current game state.
 */
public class State {
    byte[] whites = new byte[8];
    byte[] blacks = new byte[8];

    public static State getInitialState() {
        return null;
    }

    /**
     * Checks if the current state is final
     *
     * @return true is the state is final, false otherwise
     */
    boolean isFinal() {
        return whites[7] != 0 || blacks[0] != 0;
    }

    FinalStateType getFinalStateType() {
        if (whites[7] != 0)
            return FinalStateType.WHITE_WIN;
        if (blacks[0] != 0)
            return FinalStateType.BLACK_WIN;
        return FinalStateType.DRAW;
    }

    /**
     * @param who player who has to move
     * @return all accessible states starting from the current state
     */
    public List<State> getAccessibleStates(PlayerColor who) {
        List<State> accessibleStates = new ArrayList<>();

        return accessibleStates;
    }

    public List<Position> getPiecesPositionsFor(PlayerColor who) {
        List<Position> positions = new ArrayList<>();

        for (int i = 0; i < 8; i ++)
            for (int j = 0; j < 8 ;j++) {
                Position p = new Position(i, j);
                if (hasPieceOnPosition(p, who))
                    positions.add(p);
            }

        return positions;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("  1 2 3 4 5 6 7 8\n");
        char startingLetter = 'A';
        for (int i = 0; i < 8; i++) {
            builder.append(String.format("%c|%c|%c|%c|%c|%c|%c|%c|%c\n", startingLetter + i, '*', 'w', 'w', '*', 'w', '*', 'w', 'w'));
            builder.append("  - - - - - - - - \n");
        }
        return builder.toString();
    }

    public boolean canMove(Position from, Position to, PlayerColor who) {
        if (getPossibleMovesForPiece(from, who).containsKey(to))
            return true;
        else return false;
    }

    private Map<Position, Optional<Position>> getPossibleMovesForPiece(Position piecePosition, PlayerColor who) {
        int direction = 1;
        PlayerColor other = who.getOther();
        Map<Position, Optional<Position>> possibleMoves = new HashMap<>();
        if (hasPieceOnPosition(piecePosition, who)) {
            // whites move down, blakcs move up
            if (who == PlayerColor.WHITE) direction = -1;
            else direction = 1;
            // Move one step ahead, without capturing
            Position oneStepAhead = piecePosition.getNeighbourPosition(direction, 0);
            if (!hasPieceOnPosition(oneStepAhead, other)) {
                possibleMoves.put(oneStepAhead, Optional.empty());
            }
            // Move two step ahead, without capturing
            if (who.isInitial(piecePosition)) {
                Position twoStepAhead = piecePosition.getNeighbourPosition(2 * direction, 0);
                if (!hasPieceOnPosition(twoStepAhead, other) && !hasPieceOnPosition(oneStepAhead, other)) {
                    possibleMoves.put(twoStepAhead, Optional.empty());
                }
            }
            Position left = piecePosition.getNeighbourPosition(direction, 1);
            Position right = piecePosition.getNeighbourPosition(direction, -1);

            if (hasPieceOnPosition(left, other))
                possibleMoves.put(left, Optional.of(left));
            if (hasPieceOnPosition(right, other))
                possibleMoves.put(right, Optional.of(right));

            // TODO fuking en-passant
        } else {
            throw new RuntimeException("No piece at that position");
        }

        return possibleMoves;
    }

    public boolean hasPieceOnPosition(Position piecePosition, PlayerColor who) {
        int row = piecePosition.getRow();
        int column = piecePosition.getColumn();

        if (who == PlayerColor.WHITE)
            return (byte) (whites[row] & (1 << column)) != 0;
        return (byte) (whites[row] & (1 << column)) != 0;
    }

    public boolean isEmpty(Position position) {
        return !hasPieceOnPosition(position, PlayerColor.WHITE) && !hasPieceOnPosition(position, PlayerColor.BLACK);
    }

    //
    public State executeMove(Position from, Position to, PlayerColor who) {
        State nextState = this.copy();
        Map<Position, Optional<Position>> moves = nextState.getPossibleMovesForPiece(from, who);
        Optional<Position> captured = moves.get(to);
        nextState.removePiece(who, from);
        nextState.addPiece(who, to);
        captured.ifPresent(position -> nextState.removePiece(who, position));

        return nextState;
    }

    private void removePiece(PlayerColor who, Position from) {
        int col = from.getColumn();
        int row = from.getRow();

        if (who == PlayerColor.WHITE)
            whites[row] = (byte) (whites[row] & removeMask(col));
        else
            blacks[row] = (byte) (blacks[row] & removeMask(col));

    }

    private byte removeMask(int col) {
        return (byte) ~(1 << col);
    }

    private void addPiece(PlayerColor who, Position to) {
        int col = to.getColumn();
        int row = to.getRow();

        if (who == PlayerColor.WHITE)
            whites[row] = (byte) (whites[row] | addMask(col));
        else
            blacks[row] = (byte) (blacks[row] | addMask(col));
    }

    private byte addMask(int col) {
        return (byte) (1 << col);
    }

    private State copy() {
        State st = new State();
        st.blacks = Arrays.copyOf(this.blacks, 8);
        st.whites = Arrays.copyOf(this.whites, 8);
        return st;
    }
}
