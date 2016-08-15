package com.kevin.scene;

/**
 * Created by Kevin on 2016-01-23.
 */

//Holds various state relevant to a player, like the number pieces it has left, its color,
//and whether or not it can jump this turn.
public class Player {

    public PieceColor color;

    boolean canJump = false;

    int numPieces = 0;

    public Player(PieceColor color) {
        this.color = color;
    }

}
