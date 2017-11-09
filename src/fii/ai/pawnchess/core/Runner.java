package fii.ai.pawnchess.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
        currentState = this.checkUserInput();
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

//             If there was a shadow from the last turn but the other player didn't capture it
            if (shadow != null && currentState.hasPieceOnPosition(shadow, movesNext.getOther())) {
                // Delete the shadow
                currentState.removePiece(movesNext.getOther(), shadow);
                System.out.println("Deleting the copy from en-passant");
            }
            shadow = State.checkIfEnPassant(previousState, currentState);

            movesNext = movesNext.getOther();


            if (currentState.isFinal(movesNext.getOther())) {
                System.out.println(currentState.getFinalStateType().getMessage());
                break;
            }
        }
    }

    private State checkUserInput(){
        try
        {
            Scanner input = new Scanner(new FileReader("user-input"));
            if(input.next().equals("0")){
                return State.getInitialState();
            }
            byte[] whites = new byte[8];
            byte[] blacks = new byte[8];
            String value;
            for(int i=0;i<8;i++)
                for(int j=0;j<8;j++)
                {
                    value = input.next();
                    if(value.toUpperCase().equals("W")){
                        whites[i] = (byte) (whites[i] | (1 << j));
                    }
                    if(value.toUpperCase().equals("B")){
                        blacks[i] = (byte) (blacks[i] | (1 << j));
                    }
                }
            if(whites[0] != 0 || whites[7] != 0 || blacks[0] != 0 || blacks[7] != 0){
                throw new RuntimeException("Game is already finished!");
            }
            State state = new State();
            state.whites = whites;
            state.blacks = blacks;
            return state;
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return State.getInitialState();
    }
}
