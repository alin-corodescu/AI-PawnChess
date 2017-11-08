package fii.ai.pawnchess.core;

/**
 * Created by alin on 11/8/17.
 */
public interface Brain {
    /**
     * Computes the next state based on a heuristic
     * @param currentState state from which to go
     * @return next state
     */
    State computeNextState(State currentState);
}
