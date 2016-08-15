package com.kevin.scene;

/**
 * Created by Kevin on 2016-01-21.
 */

//Represents a move. Has a endpoint, whether or not the move will capture a piece, and if
//it will capture, the piece to be captured.
public class Move {

    BoardPos endPoint;
    boolean isCapture = false;
    PieceEntity target = null;

    public Move(BoardPos endPoint) {
        this.endPoint = endPoint;
    }

    public Move(BoardPos endPoint, boolean isCapture, PieceEntity target) {
        this.endPoint = endPoint;
        this.isCapture = isCapture;
        this.target = target;
    }


}
