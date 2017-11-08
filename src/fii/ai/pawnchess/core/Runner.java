package fii.ai.pawnchess.core;

import java.util.Random;

/**
 * Created by alin on 11/8/17.
 */
public class Runner {
    PlayerColor movesNext;
    State currentState;
    Brain computer = null;
    public static void main(String args[]) {
        new Runner().run();

    }

    private void run() {
        currentState = State.getInitialState();
        Random rand = new Random();
        if (rand.nextBoolean()) {
            // The player always plays with white
            movesNext = PlayerColor.WHITE;
        }
        else {
            // The computer always plays with black
            movesNext = PlayerColor.BLACK;
        }

        // TODO make more user friendly a.i. "You move first!"
        while (true) {
            if (movesNext == PlayerColor.WHITE) {
//                Gather input, make the move
            }
            else {
//              Computer moves
                currentState = computer.computeNextState(currentState);
            }

            System.out.println(currentState);

            if (currentState.isFinal()) {
                System.out.println(currentState.getFinalStateType().getMessage());
                break;
            }
        }
    }
}
