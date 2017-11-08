package fii.ai.pawnchess.core;

import java.util.List;
import java.util.Random;

/**
 * Created by alin on 11/8/17.
 */
public class Runner {
    PlayerColor movesNext;
    State currentState;
    Brain computer = new BrainImpl(4);
    MovesReader reader = new MovesReader();
    public static void main(String args[]) {
        new Runner().run();

    }

    private void run() {
        currentState = State.getInitialState();
        State previousState = currentState;
        Position shadow = null;
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
            System.out.println(currentState);
            if (movesNext == PlayerColor.WHITE) {
//                Gather input, make the move
                List<Position> transition = reader.readNextMove();
                Position from = transition.get(0);
                Position to = transition.get(1);

                if (currentState.canMove(from, to, PlayerColor.WHITE)) {
                    previousState = currentState;
                    currentState = currentState.executeMove(from, to, PlayerColor.WHITE);
                }
                else {
                    System.out.println("Invalid move, please try again!");
                    continue;
                }
            }
            else {
//              Computer moves
                previousState = currentState;
                currentState = computer.computeNextState(currentState);

            }

            // If there was a shadow from the last turn but the other player didn't capture it
            if (shadow != null && currentState.hasPieceOnPosition(shadow, movesNext.getOther())) {
                // Delete the shadow
                currentState.removePiece(movesNext.getOther(), shadow);
                System.out.println("Deleting the copy from en-passant");
            }
            shadow = State.checkIfEnPassant(previousState, currentState);

            movesNext = movesNext.getOther();


            if (currentState.isFinal()) {
                System.out.println(currentState.getFinalStateType().getMessage());
                break;
            }
        }
    }
}
