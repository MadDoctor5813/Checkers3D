package com.kevin.scene;

import com.kevin.physic.BoundingBox;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 532259 on 1/14/2016.
 */

//Represents one piece in the world
public class PieceEntity extends Entity {

    PieceColor color;

    //Holds all the selection boxes to be rendered if this piece is selected
    ArrayList<SelectionEntity> selections;

    //All the valid moves this piece can make
    ArrayList<Move> moves;

    final float PIECE_WIDTH = 1.64f;
    final float PIECE_HEIGHT = 0.365f;

    boolean king = false;

    public BoardPos getBoardPos() {
        return boardPos;
    }

    BoardPos boardPos;

    BoardEntity board;
    Player player;

    public BoundingBox getBox() {
        return box;
    }

    BoundingBox box;

    public PieceEntity(Scene scene, BoardEntity board, BoardPos boardPos, Player player) {
        super(scene, board.boardPosToWorld(boardPos),
                new Vector3f(1), new Vector3f(0),
                player.color == PieceColor.BLACK ? "blackpiece.asset" : "redpiece.asset");
        this.boardPos = boardPos;
        this.board = board;
        this.color = player.color;
        this.player = player;
        selections = new ArrayList<>();
        moves = new ArrayList<>();
        //create the bounding box for this piece
        genBoundingBox();
    }

    public Move getMove(BoardPos pos) {
        //linear search for the move with the given endpoint.
        for (Move move : moves) {
            if (move.endPoint.equals(pos)) {
                return move;
            }
        }
        return null;
    }

    public void move(BoardPos pos) {
        //set the boardpos, and calculate the world pos from it
        this.boardPos = pos;
        this.pos = board.boardPosToWorld(pos);
        //regenerate the bounding box based on the world pos
        genBoundingBox();
        //set the flag so that we will update the model matrix in the update() method
        this.matUpdateNeeded = true;
    }

    public void pruneNonJumps() {
        //we have to use this iterator, because we can't modify the list
        //while looping through it otherwise
        Iterator<Move> iter = moves.iterator();
        while (iter.hasNext()) {
            if (iter.next().isCapture == false) {
                iter.remove();
            }
        }
    }

    private void genBoundingBox() {
        //creates a box using the size constants above
        box = new BoundingBox(new Vector3f(pos).sub(PIECE_WIDTH / 2, PIECE_HEIGHT / 2, PIECE_WIDTH / 2),
                new Vector3f(pos).add(PIECE_WIDTH / 2, PIECE_HEIGHT / 2, PIECE_WIDTH / 2));
    }

    public void generateSelections() {
        selections.clear();
        //generate the selection for this piece
        addSelection(this.boardPos);
        //generate a selection box for every move this piece can make
        for (Move move : moves) {
            addSelection(move.endPoint);
        }
    }

    public void generateMoves() {
        //check for kingness
        if (color == PieceColor.BLACK && this.boardPos.row == 0) {
            makeKing();
        }
        if (color == PieceColor.RED && this.boardPos.row == 7) {
            makeKing();
        }
        moves.clear();
        //generate the moves for each color. Kings can move in any direction, so they get
        //red and black moves
        if (this.color == PieceColor.BLACK || king) {
            genMovesBlack();
        }
        if (this.color == PieceColor.RED || king) {
            genMovesRed();
        }
    }

    private void makeKing() {
        king = true;
        //flip the piece over
        this.rot.x = (float)Math.toRadians(180);
    }

    private void genMovesRed() {
        //Check moves to the top right
        BoardPos topRight = new BoardPos(this.boardPos).addUp(1).addRight(1);
        PieceEntity topRightPiece = board.findPiece(topRight);
        //if there's no piece in the way and the position is valid
        if (topRightPiece == null) {
            if (topRight.isValid()) {
                //add the move
                moves.add(new Move(topRight));
            }
        }
        else {
            BoardPos topRightCap = new BoardPos(topRight).addUp(1).addRight(1);
            //if the top right capture pos is valid, and the top right piece's color is opposite to ours
            if (topRightCap.isValid() && topRightPiece.color != this.color && board.findPiece(topRightCap) == null) {
                moves.add(new Move(topRightCap, true, topRightPiece));
                //notify the player that we can jump this turn
                player.canJump = true;
            }
        }
        //as above, but for the top left
        BoardPos topLeft = new BoardPos(this.boardPos).addUp(1).addLeft(1);
        PieceEntity topLeftPiece = board.findPiece(topLeft);
        if (topLeftPiece == null) {
            if (topLeft.isValid()) {
                moves.add(new Move(topLeft));
            }
        }
        else {
            BoardPos topLeftCap = new BoardPos(topLeft).addUp(1).addLeft(1);
            if (topLeftCap.isValid() && topLeftPiece.color != this.color && board.findPiece(topLeftCap) == null) {
                moves.add(new Move(topLeftCap, true, topLeftPiece));
                player.canJump = true;

            }
        }
    }

    private void genMovesBlack() {
        //as above, but for the bottom right
        BoardPos bottomRight = new BoardPos(this.boardPos).addDown(1).addRight(1);
        PieceEntity bottomRightPiece = board.findPiece(bottomRight);
        if (bottomRightPiece == null) {
            if (bottomRight.isValid()) {
                moves.add(new Move(bottomRight));
            }
        }
        else {
            BoardPos bottomRightCap = new BoardPos(bottomRight).addDown(1).addRight(1);
            if (bottomRightCap.isValid() && bottomRightPiece.color != this.color && board.findPiece(bottomRightCap) == null) {
                moves.add(new Move(bottomRightCap, true, bottomRightPiece));
                player.canJump = true;

            }
        }
        //as above, but for the bottom left
        BoardPos bottomLeft = new BoardPos(this.boardPos).addDown(1).addLeft(1);
        PieceEntity bottomLeftPiece = board.findPiece(bottomLeft);
        if (bottomLeftPiece == null) {
            if (bottomLeft.isValid()) {
                moves.add(new Move(bottomLeft));
            }
        }
        else {
            BoardPos bottomLeftCap = new BoardPos(bottomLeft).addDown(1).addLeft(1);
            if (bottomLeftCap.isValid() && bottomLeftPiece.color != this.color && board.findPiece(bottomLeftCap) == null) {
                moves.add(new Move(bottomLeftCap, true, bottomLeftPiece));
                player.canJump = true;
            }
        }
    }

    private void addSelection(BoardPos pos) {
        selections.add(new SelectionEntity(board, pos));
    }

    public void renderSelection(Scene scene) {
        //for every selection box, render it
        for (SelectionEntity selection : selections) {
            selection.render(scene);
        }
    }

}
