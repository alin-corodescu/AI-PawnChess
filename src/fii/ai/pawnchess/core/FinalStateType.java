package fii.ai.pawnchess.core;

/**
 * Created by alin on 11/8/17.
 */
public enum FinalStateType {
    WHITE_WIN("White won the game!"), BLACK_WIN("Black won the game!"), DRAW("It's a draw!");

    FinalStateType(String message) {
        this.message = message;
    }

    String message;

    public String getMessage() {
        return message;
    }
}
