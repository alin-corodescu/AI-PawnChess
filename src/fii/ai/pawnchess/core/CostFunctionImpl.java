package fii.ai.pawnchess.core;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CostFunctionImpl implements CostFunction
{
    @Override
    public double computeCost(State state)
    {
        return this.howManyStepsBeforeFinalPosition(state);
    }

    private int howManyStepsBeforeFinalPosition(State state)
    {
        List<Position> whitePositions = state.getPiecesPositionsFor(PlayerColor.WHITE);
        List<Position> blackPositions = state.getPiecesPositionsFor(PlayerColor.BLACK);
        List<Position> blacks;
        List<Position> whites;
        Optional<Position> blackPosition;
        Optional<Position> whitePosition;
        int result = 0;
        for (int j = 0; j <= 8; j++)
        {
            int finalJ = j;

            blacks = blackPositions.stream().filter(x -> x.getColumn() == finalJ).collect(Collectors.toList());
            blackPosition = blacks.stream().max(Comparator.comparing(Position::getRow));

            whites = whitePositions.stream().filter(x -> x.getColumn() == finalJ).collect(Collectors.toList());
            whitePosition = whites.stream().max(Comparator.comparing(Position::getRow));

            if(blackPosition.isPresent()){
                blackPositions.remove(blackPosition.get());
                if(whitePosition.isPresent()){
                    if(whitePosition.get().getRow() < blackPosition.get().getRow())
                        result -= whitePosition.get().getRow() * 10;
                }
                result +=  blackPosition.get().getRow();
            }
        }
        return result;
    }
}
