package fii.ai.pawnchess.core;

import java.util.List;
import java.util.Random;

public class Tree
{
    private Node rootNode;
    private int lookAhead;
    //*TODO use implementation of the costFunction and not null
    private CostFunction costFunction;

    Tree() {
        this.rootNode = null;
        lookAhead = 0;
        costFunction = null;
    }

    Tree(Node rootNode, int lookAhead) {
        this.rootNode = rootNode;
        this.lookAhead = lookAhead;
        costFunction = (x) -> new Random().nextInt(10)+1;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public void createTreeOfStatesUsingDFS(Node node, int level, PlayerColor color){
//      When reached the lookahead-th level of tree, compute the cost function
        if(level == lookAhead ){
            node.setScore(this.costFunction.computeCost(node.getState()));
            return;
        }
//      if not reached lookahead-th level then create the next level
        this.createNodeChildren(node, color);
        PlayerColor currentColor = PlayerColor.values()[PlayerColor.WHITE.ordinal() + PlayerColor.BLACK.ordinal() - color.ordinal()];
        List<Node> children = node.getChildren();
        for(Node child : children){
            createTreeOfStatesUsingDFS(child, level+1, currentColor);
        }
//      decide which the parent node score will be based on the currentColour
        if(level %2 == 0){
            node.setScore(node.getChildren().stream().mapToDouble(Node::getScore).max().getAsDouble());
            System.out.println(node.getScore());
        }
        else{
            node.setScore(node.getChildren().stream().mapToDouble(Node::getScore).min().getAsDouble());
        }

    }

    private void createNodeChildren(Node currentNode, PlayerColor color){
        List<State> neighbours = currentNode.getState().getAccessibleStates(color);
        for(State state : neighbours){
            Node node = new Node(state, 0);
            currentNode.addChildNode(node);
        }
    }
}
