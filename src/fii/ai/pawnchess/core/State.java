package fii.ai.pawnchess.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds information about the current game state.
 */
public class State {
    byte[] whites, blacks;
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

    //
    public State executeMove(Position from, Position to) {
        // Todo, implement checks
        State nextState = new State();
        return nextState;
    }
}
