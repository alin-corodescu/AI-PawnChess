package fii.ai.pawnchess.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by alin on 11/8/17.
 */
public class MovesReader {
    private Scanner scanner = new Scanner(System.in);

    public List<Position> readNextMove() {
        System.out.println("Please specify your move: ");

        String from = scanner.next();
        String to = scanner.next();

        List<Position> transition = new ArrayList<>();
        transition.add(new Position(from));
        transition.add(new Position(to));

        return transition;
    }
}
