package fii.ai.pawnchess.core;


import javafx.geometry.Pos;

/**
 * Created by alin on 11/8/17.
 * Class represeting the position of a piece on the chess table
 */
public class Position {
    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    private int column, row;

    /**
     * Constructor which gets both the row and columns as int's
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Constructor taking a string as a parameter, formatted as [row_as_letter][column] e.g. : "A8"
     * @param pos position string formatted as described above
     */
    public Position(String pos) {
        char row = pos.charAt(0);
        int column = Integer.valueOf("" + pos.charAt(1));
        this.row = row - 'A';
        this.column = column - 1;
    }

    @Override
    public String toString() {
        return row + " " + column;
    }

    @Override
    public int hashCode() {
        return (row + " " + column).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position pos = (Position) obj;
            return pos.row == row && pos.column == column;
        }
        return super.equals(obj);
    }

    public Position getNeighbourPosition(int dx, int dy) {
        Position newPosition = new Position(this.row, this.column);
        newPosition.column += dy;
        newPosition.row += dx;

        return newPosition;
    }

}
