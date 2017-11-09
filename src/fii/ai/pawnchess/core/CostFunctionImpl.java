package fii.ai.pawnchess.core;

import java.util.List;

public class CostFunctionImpl implements CostFunction
{
    private List<Position> whitePositions;
    private List<Position> blackPositions;
    public static int FINAL_SCORE = 100000, DRAW_SCORE = 0, BEST_SCORE = 100, WORST_SCORE = -100,
            GOOD_SCORE = 50, BAD_SCORE = -50;

    @Override
    public double computeCost(State state)
    {
        this.whitePositions = state.getPiecesPositionsFor(PlayerColor.WHITE);
        this.blackPositions = state.getPiecesPositionsFor(PlayerColor.BLACK);
        double result = 0;
        // Hai sa lasam ponderile sa fie
        result += this.finalBlow(state) * 100;
        result += this.strike(state) * 10;
        result += this.teamCount();
        result += this.pawnStructure(state) * 0.3 ;
        result += this.pawnAdvancement();
        return result;
    }

    private double pawnAdvancement() {
        // return the square of the distances to reward more advanced pieces more
        return blackPositions.stream().mapToInt(x -> (6 - x.getRow()) * (6 - x.getRow()) * GOOD_SCORE).sum() +
                whitePositions.stream().mapToInt(x -> (x.getRow() - 2) * (x.getRow() - 2) * BAD_SCORE).sum();
    }

    private int finalBlow(State state)
    {
        FinalStateType finalStateType;
        // Nu conteaza cine e la mutare, daca castiga unul din ei asignam scorul care trb
        if (state.isFinal(PlayerColor.BLACK) || state.isFinal(PlayerColor.WHITE))
        {
            finalStateType = state.getFinalStateType();
            if (finalStateType == FinalStateType.WHITE_WIN)
                return WORST_SCORE;
            if (finalStateType == FinalStateType.BLACK_WIN)
                return BEST_SCORE;
        }
        return 0;
    }

    private int pawnStructure(State state)
    {
        int result = DRAW_SCORE;
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
                result += GOOD_SCORE * 3;
            else
            {
                if (state.hasPieceOnPosition(leftNeighbour, PlayerColor.WHITE))
                    result += GOOD_SCORE;
                if (state.hasPieceOnPosition(rightNeighbour, PlayerColor.BLACK))
                    result += GOOD_SCORE;
            }
        }
        return result;
    }

    // only to be taken into account if we have an even lookahead
    private int teamCount()
    {
        int score = DRAW_SCORE;
        if (blackPositions.size() > whitePositions.size())
            score += GOOD_SCORE * (blackPositions.size() - whitePositions.size());
        if (blackPositions.size() < whitePositions.size())
            score -= BAD_SCORE * (blackPositions.size() - whitePositions.size());
        return score;
    }

    private int strike(State state)
    {
        int score = DRAW_SCORE;
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
                    score += BEST_SCORE*BEST_SCORE;
                else
                    score -= BAD_SCORE * checkIfDefenders(state, leftNeighbour);
                if (checkIfDefenders(state, rightNeighbour) == 0)
                    score += BEST_SCORE*BEST_SCORE;
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
