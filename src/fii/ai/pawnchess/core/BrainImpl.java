package fii.ai.pawnchess.core;

import java.util.Comparator;

/**
 * Created by alin on 11/8/17.
 */
public class BrainImpl implements Brain {

    public BrainImpl(int lookAhead) {
        this.lookAhead = lookAhead;
    }
    private int lookAhead = 2;

    @Override
    public State computeNextState(State currentState) {
        Node rootNode = new Node();
        rootNode.setState(currentState);
        Tree statesTree = new Tree(rootNode, 2);
        statesTree.createTreeOfStatesUsingDFS(statesTree.getRootNode(), 0, PlayerColor.BLACK);

        Node bestChild = rootNode.getChildren().stream().max(Comparator.comparingDouble(Node::getScore)).get();
        return bestChild.getState();
    }
}
