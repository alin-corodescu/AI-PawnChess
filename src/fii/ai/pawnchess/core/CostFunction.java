package fii.ai.pawnchess.core;

/**
 * Created by alin on 11/8/17.
 */
public interface CostFunction {

    /**
     * Computes the cost function for this state
     * @param state the state for which to compute the cost
     * @return the cost associated with this state
     */
    double computeCost(State state);
}
