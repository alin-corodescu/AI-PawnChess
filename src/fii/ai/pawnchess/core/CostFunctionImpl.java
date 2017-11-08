package fii.ai.pawnchess.core;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CostFunctionImpl implements CostFunction
{
    private List<Position> whitePositions;
    private List<Position> blackPositions;

    @Override
    public double computeCost(State state)
    {
        this.whitePositions = state.getPiecesPositionsFor(PlayerColor.WHITE);
        this.blackPositions = state.getPiecesPositionsFor(PlayerColor.BLACK);
        double result = 0;
//        result += this.howManyStepsBeforeFinalPosition(state)*0.2;
        result += this.pawnStructure(state)*0.8;
        return result;
    }

    private int howManyStepsBeforeFinalPosition(State state)
    {
        List<Position> blacks;
        List<Position> whites;
        Optional<Position> blackPosition;
        Optional<Position> whitePosition;
        int result = 0;
        for (int j = 0; j <= 8; j++)
        {
            int finalJ = j;

            blacks = this.blackPositions.stream().filter(x -> x.getColumn() == finalJ).collect(Collectors.toList());
            blackPosition = blacks.stream().max(Comparator.comparing(Position::getRow));

            whites = this.whitePositions.stream().filter(x -> x.getColumn() == finalJ).collect(Collectors.toList());
            whitePosition = whites.stream().max(Comparator.comparing(Position::getRow));

            if (blackPosition.isPresent())
            {
                this.blackPositions.remove(blackPosition.get());
                if (whitePosition.isPresent())
                {
                    if (whitePosition.get().getRow() < blackPosition.get().getRow())
                        result -= whitePosition.get().getRow() * 2 * whitePosition.get().getRow();
                }
                result += (8 - blackPosition.get().getRow()) * 2;
            }
        }
        return result;
    }

    private int pawnStructure(State state)
    {
        int result = 0;
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
}
