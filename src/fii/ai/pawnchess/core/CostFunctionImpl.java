package fii.ai.pawnchess.core;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CostFunctionImpl implements CostFunction
{
    private List<Position> whitePositions;
    private List<Position> blackPositions;
    public static int DRAW_SCORE = 0, GOOD_SCORE = 1, BAD_SCORE = -1;

    @Override
    public double computeCost(State state)
    {
        GOOD_SCORE = 1;
        BAD_SCORE = -1;
        this.whitePositions = state.getPiecesPositionsFor(PlayerColor.WHITE);
        this.blackPositions = state.getPiecesPositionsFor(PlayerColor.BLACK);
        if(this.blackPositions.size() > this.whitePositions.size()+1)
            GOOD_SCORE = 2;
        if(this.whitePositions.size() < this.whitePositions.size()+1)
            BAD_SCORE = -2;

        double result = 0;
        
        //the finish state
        result += this.finalState(state);

        //state when a pawn is removed from the game
        result += this.teamCount();

        //state where pawns are nearer to finish
        result += this.howManyStepsBeforeFinalPosition(state);

        //state with a isolated enemy pawn
        result += this.pawnStrike(state);

        //state where a pawn is protected by other pawns behind him
        result += this.pawnAlliance(state);

        //states where pawns are closer to the finish line
        result += this.pawnAdvancement();

        return result;
    }

    private double pawnAdvancement() {
        // return the square of the distances to reward more advanced pieces more
        return blackPositions.stream().mapToInt(x -> (6 - x.getRow()) * (6 - x.getRow()) * GOOD_SCORE).sum() +
                whitePositions.stream().mapToInt(x -> (x.getRow() - 2) * (x.getRow() - 2) * BAD_SCORE).sum();
    }

    private int finalState(State state)
    {
        FinalStateType finalStateType;
        if (state.isFinal(PlayerColor.BLACK) || state.isFinal(PlayerColor.WHITE))
        {
            finalStateType = state.getFinalStateType();
            if (finalStateType == FinalStateType.WHITE_WIN)
                return Integer.MIN_VALUE;
            if (finalStateType == FinalStateType.BLACK_WIN)
                return Integer.MAX_VALUE;
        }
        return 0;
    }

    private int pawnAlliance(State state)
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

            if (state.hasPieceOnPosition(leftNeighbour, PlayerColor.WHITE))
                result += GOOD_SCORE;
            if (state.hasPieceOnPosition(rightNeighbour, PlayerColor.BLACK))
                result += GOOD_SCORE;
        }
        return result;
    }

    // only to be taken into account if we have an even lookahead
    private int teamCount()
    {
        int score = DRAW_SCORE;
        score += GOOD_SCORE * blackPositions.size() ;
        score += BAD_SCORE * whitePositions.size();
        return score;
    }

    private int pawnStrike(State state)
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
                    score += GOOD_SCORE;
                else
                    score += BAD_SCORE * checkIfDefenders(state, leftNeighbour);
                if (checkIfDefenders(state, rightNeighbour) == 0)
                    score += GOOD_SCORE;
                else
                    score += BAD_SCORE * checkIfDefenders(state, rightNeighbour);
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

    private int howManyStepsBeforeFinalPosition(State state)
    {
        List<Position> blacks;
        List<Position> whites;
        Optional<Position> blackPosition;
        Optional<Position> whitePosition;
        Position leftNeighbour = new Position(-1, -1);
        Position rightNeighbour = new Position(-1, -1);
        int score = 0;
        for (int j = 0; j <= 8; j++)
        {
            int finalJ = j;

            blacks = this.blackPositions.stream().filter(x -> x.getColumn() == finalJ).collect(Collectors.toList());
            blackPosition = blacks.stream().min(Comparator.comparing(Position::getRow));

            whites = this.whitePositions.stream().filter(x -> x.getColumn() == finalJ).collect(Collectors.toList());
            whitePosition = whites.stream().max(Comparator.comparing(Position::getRow));

            //check if we have a black pawn on the j-th column
            if (blackPosition.isPresent())
            {
                this.blackPositions.remove(blackPosition.get());

                //check if we have a white pawn also on j-th column
                if (whitePosition.isPresent())
                {
                    if (whitePosition.get().getRow() > blackPosition.get().getRow())
                        score += BAD_SCORE;
                }
                leftNeighbour.setRow(blackPosition.get().getRow() -1);
                rightNeighbour.setRow(blackPosition.get().getRow() - 1);

                leftNeighbour.setColumn(blackPosition.get().getRow() - 1);
                rightNeighbour.setColumn(blackPosition.get().getColumn() + 1);
                if(this.whitePositions.contains(leftNeighbour) && this.whitePositions.contains(rightNeighbour)){
                    score += BAD_SCORE;
                }
                else{
                    score += Integer.MAX_VALUE;
                }
            }
        }
        return score;
    }
}
