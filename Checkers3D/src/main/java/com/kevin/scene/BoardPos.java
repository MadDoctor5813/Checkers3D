package com.kevin.scene;

/**
 * Created by 532259 on 1/21/2016.
 */

//Represents a position on the board
public class BoardPos {

    public int row;
    public int col;

    public BoardPos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    //inits from another boardpos
    public BoardPos(BoardPos other) {
        this.row = other.row;
        this.col = other.col;
    }

    public void set(int row, int col) {
        this.row = row;
        this.col = col;
    }

    //checks if this pos is off the board, i.e, its not negative or over 7
    public boolean isValid() {
        return (this.col >= 0 && this.col < 8 && this.row >= 0 && this.row < 8);
    }

    //convenience methods for getting boardPos's from directions
    public BoardPos addLeft(int delta) {
        this.col += delta;
        return this;
    }

    public BoardPos addRight(int delta) {
        this.col -= delta;
        return this;
    }

    public BoardPos addDown(int delta) {
        this.row -= delta;
        return this;
    }

    public BoardPos addUp(int delta) {
        this.row += delta;
        return this;
    }

    //equals and hashCode, provided by IntelliJ
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardPos boardPos = (BoardPos) o;

        if (row != boardPos.row) return false;
        return col == boardPos.col;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}
