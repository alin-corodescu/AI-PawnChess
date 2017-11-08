package fii.ai.pawnchess.core;

import java.util.ArrayList;
import java.util.List;

public class Node
{
    private double score;
    private State state;
    private List<Node> children;

    Node(){
        score = 0;
        children = new ArrayList<>();
        state = new State();
    }

    Node(State state, double score){
        this.state = state;
        this.score = score;
        children = new ArrayList<>();
    }

    public double getScore()
    {
        return score;
    }

    public List<Node> getChildren()
    {
        return children;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public State getState()
    {

        return state;
    }

    public void addChildNode(Node childNode){
        this.children.add(childNode);
    }

    public void addChildren(List<Node> children){
        this.children = children;
    }

}
