package fii.ai.pawnchess.core;

/**
 * Created by alin on 11/8/17.
 */
public enum PlayerColor {
    WHITE, BLACK;

    public boolean isInitial(Position p) {
        return (this == WHITE && p.getRow() == 1) || (this == BLACK && p.getRow() == 6);
    }

    public PlayerColor getOther() {
        if (this == WHITE)
            return BLACK;
        return WHITE;
    }
}
