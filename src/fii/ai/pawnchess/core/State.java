package fii.ai.pawnchess.core;

import java.util.*;

/**
 * Holds information about the current game state.
 */
public class State {
    byte[] whites = new byte[8];
    byte[] blacks = new byte[8];
    /**
     * Checks if the transition between the current state and the next state is valid
     * @return true if the transition is valid, false otherwise
     */
    boolean checkIfValidMove(State nextState, PlayerColor who) {
        throw new RuntimeException("Not implemtented");
    }

    /**
     * Checks if the current state is final
     * @return true is the state is final, false otherwise
     */
    boolean isFinal() {
        throw new RuntimeException("Not implemtented");
    }

    FinalStateType getFinalStateType() {
        return FinalStateType.DRAW;
    }

    /**
     * @param who player who has to move
     * @return all accessible states starting from the current state
     */
    public List<State> getAccessibleStates(PlayerColor who)
    {
        List<State> accessibleStates = new ArrayList<>();

        return accessibleStates;
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

    public static State getInitialState() {
        return null;
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
                Position twoStepAhead = piecePosition.getNeighbourPosition(2*direction, 0);
                if (!hasPieceOnPosition(twoStepAhead, other) && !hasPieceOnPosition(oneStepAhead, other)) {
                    possibleMoves.put(twoStepAhead, Optional.empty());
                }
            }
            Position left = piecePosition.getNeighbourPosition(direction, 1);
            Position right = piecePosition.getNeighbourPosition(direction, -1);

            if (hasPieceOnPosition(left, other))
                possibleMoves.put(left, Optional.of(left));
            if (hasPieceOnPosition(right,other))
                possibleMoves.put(right, Optional.of(right));

            // TODO fuking en-passant
        }
        else {
            throw new RuntimeException("No piece at that position");
        }

        return possibleMoves;
    }

    private boolean hasPieceOnPosition(Position piecePosition, PlayerColor who) {
        return true;
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

        if (who == PlayerColor.WHITE) {
            whites[row] = (whites[row] & removeMask(col))
        }
    }

    private byte removeMask(int col) {
        return (byte)~(1 << col);
    }

    private void addPiece(PlayerColor who, Position to) {

    }

    private State copy() {
        State st = new State();
        st.blacks = Arrays.copyOf(this.blacks, 8);
        st.whites = Arrays.copyOf(this.whites, 8);
    }
}
