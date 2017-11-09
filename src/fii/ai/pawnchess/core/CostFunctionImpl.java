package fii.ai.pawnchess.core;

import java.util.List;

public class CostFunctionImpl implements CostFunction
{
    private List<Position> whitePositions;
    private List<Position> blackPositions;
    private static int BEST_SCORE = 50, WORST_SCORE = -50, GOOD_SCORE = 5, BAD_SCORE = -5, NO_SCORE = 0;

    @Override
    public double computeCost(State state)
    {
        this.whitePositions = state.getPiecesPositionsFor(PlayerColor.WHITE);
        this.blackPositions = state.getPiecesPositionsFor(PlayerColor.BLACK);
        double result = 0;
        result += this.finalBlow(state) * 100;
        result += this.strike(state) * 50;
        result += this.teamCount() * 10;
        result += this.pawnStructure(state) * 10 ;
        return result;
    }

    private int finalBlow(State state)
    {
        if (state.isFinal(PlayerColor.BLACK))
            return BEST_SCORE;
        if (state.isFinal(PlayerColor.WHITE))
            return WORST_SCORE;
        return 0;
    }

    private int pawnStructure(State state)
    {
        int result = NO_SCORE;
        Position leftNeighbour = new Position(-1, -1);
        Position rightNeighbour = new Position(-1, -1);
        for (Position position : blackPositions)
        {
            leftNeighbour.setRow(position.getRow() - 1);
            rightNeighbour.setRow(position.getRow() - 1);

            leftNeighbour.setColumn(position.getRow() - 1);
            rightNeighbour.setColumn(position.getColumn() + 1);
            if (state.hasPieceOnPosition(leftNeighbour, PlayerColor.BLACK)
                    && state.hasPieceOnPosition(rightNeighbour, PlayerColor.BLACK))
                result += 300;
            else
            {
                if (state.hasPieceOnPosition(leftNeighbour, PlayerColor.WHITE))
                    result += 100;
                if (state.hasPieceOnPosition(rightNeighbour, PlayerColor.BLACK))
                    result += 100;
            }
        }
        return result;
    }

    // only to be taken into account if we have an even lookahead
    private int teamCount()
    {
        int score = NO_SCORE;
        if (blackPositions.size() > whitePositions.size())
            score += GOOD_SCORE;
        if (blackPositions.size() < whitePositions.size())
            score -= BAD_SCORE;
        return score;
    }

    private int strike(State state)
    {
        int score = NO_SCORE;
        Position leftNeighbour = new Position(-1, -1);
        Position rightNeighbour = new Position(-1, -1);
        for (Position position : blackPositions)
        {
            leftNeighbour.setRow(position.getRow() - 1);
            rightNeighbour.setRow(position.getRow() - 1);
            leftNeighbour.setColumn(position.getRow() - 1);
            rightNeighbour.setColumn(position.getColumn() + 1);


            if (state.hasPieceOnPosition(leftNeighbour, PlayerColor.WHITE)
                    || state.hasPieceOnPosition(rightNeighbour, PlayerColor.WHITE))
            {
                if (checkIfDefenders(state, leftNeighbour) == 0)
                    score += BEST_SCORE;
                else
                    score -= BAD_SCORE * checkIfDefenders(state, leftNeighbour);
                if (checkIfDefenders(state, rightNeighbour) == 0)
                    score += BEST_SCORE;
                else
                    score -= BAD_SCORE * checkIfDefenders(state, rightNeighbour);
            }
        }
        return score;
    }

    private int checkIfDefenders(State state, Position defended)
    {
        int k = 1, howManyDefenders = 0;
        Position defender = new Position(defended.getRow() - 1, defended.getColumn() - 1);
        for (int i = 0; i < 2; i++)
        {
            k = -k;
            defender.setColumn(k);
            if (state.hasPieceOnPosition(defender, PlayerColor.WHITE))
                howManyDefenders++;
        }
        return howManyDefenders;
    }
}
