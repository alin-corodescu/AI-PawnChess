package fii.ai.pawnchess.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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

//        Node bestChild = selectNodeMax(rootNode.getChildren());
        Node bestChild = selectNodeRandom(rootNode.getChildren());
        return bestChild.getState();
    }

    private Node selectNodeRandom(List<Node> nodes) {
        double sum = nodes.stream().mapToDouble(Node::getScore).sum();
        List<Double> probabilities = new ArrayList<>();

        for (Node node : nodes) {
            probabilities.add(node.getScore() / sum);
        }

        Random random = new Random();
        double choice = random.nextDouble();
        double runningSum = 0;
        int idx = 0;
        while (runningSum < choice && idx < probabilities.size()) {
            runningSum += probabilities.get(idx);
            idx += 1;
        }
        return nodes.get(idx-1);
    }

    private Node selectNodeMax(List<Node> nodes) {
        return nodes.stream().max(Comparator.comparingDouble(Node::getScore)).get();
    }
}
