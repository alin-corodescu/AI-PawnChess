package fii.ai.pawnchess.core;


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
        this.row = row - 1;
        this.column = column - 1;
    }

    /**
     * Constructor taking a string as a parameter, formatted as [row_as_letter][column] e.g. : "A8"
     * @param pos position string formatted as described above
     */
    public Position(String pos) {
        char row = pos.charAt(0);
        int column = Integer.valueOf("" + pos.charAt(0));
        this.row = row - 'A';
        this.column = column - 1;
    }

    public Position getNeighbourPosition(int dx, int dy) {
        Position newPosition = new Position(this.row, this.column);
        newPosition.column += dy;
        newPosition.row += dx;

        return newPosition;
    }

}
