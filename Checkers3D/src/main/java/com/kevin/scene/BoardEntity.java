package com.kevin.scene;

import com.kevin.graphics.FullScreenRenderer;
import com.kevin.physic.BoundingBox;
import com.kevin.physic.Ray;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 532259 on 1/12/2016.
 */
public class BoardEntity extends Entity {

    //positional and size constants in world units
    final float SQUARE_WIDTH = 2f;
    final float BOARD_HEIGHT = 0.22f;
    final float HEIGHT_OFFSET = 0.10f;
    final int NUM_SQUARES = 8;
    final float BOARD_SIZE = NUM_SQUARES * SQUARE_WIDTH;

    Player redPlayer;
    Player blackPlayer;

    //This is a reference to either the red or black player, not an actual third player
    Player currentPlayer;

    private HashMap<BoardPos, BoundingBox> squareHitboxes;

    //Piece array
    private ArrayList<PieceEntity> pieces;

    PieceEntity selectedPiece;

    public BoardEntity(CheckersScene scene, Vector3f pos, Vector3f scale, Vector3f rot) {
        super(scene, pos, scale, rot, "board.asset");
        pieces = new ArrayList<>();
        squareHitboxes = new HashMap<>();
        //init the board
        resetBoard();
        //generate hitboxes for squares
        genHitboxes();
    }

    @Override
    public void update() {
        super.update();
        for (PieceEntity piece : pieces) {
            piece.update();
        }

    }

    private void endTurn() {
        //check if either player has lost the game
        if (redPlayer.numPieces == 0) {
            //switch the scene state to victory
            scene.switchState(SceneState.VICTORY);
            //reset the board
            resetBoard();
        }
        if (blackPlayer.numPieces == 0) {
            //switch the scene state to victory
            scene.switchState(SceneState.VICTORY);
            //reset the board
            resetBoard();
        }
        //flip the current player
        if (currentPlayer == redPlayer) {
            currentPlayer = blackPlayer;
        }
        else if (currentPlayer == blackPlayer){
            currentPlayer = redPlayer;
        }
        //generate moves for everyone
        updateAllMoves();
    }

    private void updateAllMoves() {
        //recalculate everything move related
        blackPlayer.canJump = false;
        redPlayer.canJump = false;
        for (PieceEntity piece : pieces) {
            piece.generateMoves();
        }
        //we have to do this after all the moves have been generated, so its in a separate loop
        for (PieceEntity piece : pieces) {
            if (piece.player.canJump) {
                piece.pruneNonJumps();
            }
            piece.generateSelections();
        }
    }

    public void procMouseRay(Ray ray) {
        PieceEntity clicked = getClickedPiece(ray);
        //if we haven't clicked on a piece
        if (clicked == null) {
            //check which square we clicked
            BoardPos pos = getClickedSquare(ray);
            // if we clicked on a valid square and a piece is selected
            if (pos != null && selectedPiece != null) {
                //get a move based on which piece we clicked
                Move move = selectedPiece.getMove(pos);
                //if the move is valid
                if (move != null) {
                    //move the piece
                    selectedPiece.move(pos);
                    //if the move is a capture, remove the captured piece
                    if (move.isCapture) {
                        //deduct the piece count of the player the captured piece belongs to.
                        deletePiece(move.target);
                        //check for chain jumps by generating moves, and removing ones that aren't jumps
                        selectedPiece.generateMoves();
                        selectedPiece.pruneNonJumps();
                        selectedPiece.generateSelections();
                        //if there aren't any moves left for the piece, end the turn and deselect it
                        if (selectedPiece.moves.size() == 0) {
                            endTurn();
                            selectedPiece = null;
                        }
                    }
                    //otherwise
                    else {
                        endTurn();
                        //deselect the peice
                        selectedPiece = null;
                    }
                }
                else {
                    //otherwise, deselect the piece
                    selectedPiece = null;
                }
            }
        }
        //otherwise, select the clicked piece if its our turn to move it
        else {
            if (currentPlayer.color == clicked.color) {
                selectedPiece = clicked;
            }
        }

    }


    @Override
    public void render(Scene scene) {
        //let the superclass render the board
        super.render(scene);
        //render every piece
        for (PieceEntity piece : pieces) {
            piece.render(scene);
        }
        //if a piece is selecteed, tell it to render its selection boxes
        if (selectedPiece != null) {
            selectedPiece.renderSelection(scene);
        }
    }

    public void addPiece(int row, int column, Player player) {
        PieceEntity entity = new PieceEntity(scene, this, new BoardPos(row, column), player);
        pieces.add(entity);
    }

    public PieceEntity findPiece(BoardPos pos) {
        //linear search for a piece. Hint, hint, Mr.Wray
        for (PieceEntity piece : pieces) {
            if (piece.getBoardPos().row == pos.row && piece.getBoardPos().col == pos.col) {
                return piece;
            }
        }
        return null;
    }

    //returns the world space coordinates of the center of the square specified by the row and column
    public Vector3f boardPosToWorld(BoardPos boardPos) {
        Vector3f world = new Vector3f(pos.x + (SQUARE_WIDTH * boardPos.col),
                pos.y + BOARD_HEIGHT,
                pos.z + (SQUARE_WIDTH * boardPos.row));
        //subtract half the board size in the x and y because the origin of the board is in the center
        world.sub(BOARD_SIZE / 2, 0, BOARD_SIZE / 2);
        //add half the square size to get the center
        world.add(SQUARE_WIDTH / 2, 0, SQUARE_WIDTH / 2);
        return world;
    }

    public void deletePiece(PieceEntity piece) {
        piece.player.numPieces--;
        pieces.remove(piece);
    }

    private PieceEntity getClickedPiece(Ray ray) {
        //find the closest collision
        PieceEntity closestPiece = null;
        float closestDist = -1;
        for (PieceEntity piece : pieces) {
            float collisionDist = piece.getBox().rayCollision(ray);
            //if there is a collision
            if (collisionDist != -1) {
                //and its closer than the closest one found...
                if (closestDist < collisionDist) {
                    //set the closest distance and closestPiece piece
                    closestDist = collisionDist;
                    closestPiece = piece;
                }
            }
        }
        return closestPiece;
    }

    public void resetBoard() {
        pieces.clear();
        //init player objects
        redPlayer = new Player(PieceColor.RED);
        blackPlayer = new Player(PieceColor.BLACK);
        //give both players the proper amount of pieces
        addPiece(0, 1, redPlayer);
        addPiece(0, 3, redPlayer);
        addPiece(0, 5, redPlayer);
        addPiece(0, 7, redPlayer);
        addPiece(1, 0, redPlayer);
        addPiece(1, 2, redPlayer);
        addPiece(1, 4, redPlayer);
        addPiece(1, 6, redPlayer);
        addPiece(2, 1, redPlayer);
        addPiece(2, 3, redPlayer);
        addPiece(2, 5, redPlayer);
        addPiece(2, 7, redPlayer);
        addPiece(5, 0, blackPlayer);
        addPiece(5, 2, blackPlayer);
        addPiece(5, 4, blackPlayer);
        addPiece(5, 6, blackPlayer);
        addPiece(6, 1, blackPlayer);
        addPiece(6, 3, blackPlayer);
        addPiece(6, 5, blackPlayer);
        addPiece(6, 7, blackPlayer);
        addPiece(7, 0, blackPlayer);
        addPiece(7, 2, blackPlayer);
        addPiece(7, 4, blackPlayer);
        addPiece(7, 6, blackPlayer);
        redPlayer.numPieces = 12;
        blackPlayer.numPieces = 12;
        //set the current player to red, because red plays first
        currentPlayer = redPlayer;
        //update the moves
        updateAllMoves();
    }

    private BoardPos getClickedSquare(Ray ray) {
        BoardPos closestSquare = null;
        float closestDist = -1;
        //for every square
        for (BoardPos square : squareHitboxes.keySet()) {
            BoundingBox box = squareHitboxes.get(square);
            float collisionDist = box.rayCollision(ray);
            //if there is a collision
            if (collisionDist != -1) {
                //and its closer than the closest one found...
                if (closestDist < collisionDist) {
                    //set the closest distance and the closest square
                    closestDist = collisionDist;
                    closestSquare = square;
                }
            }
        }
        return closestSquare;
    }

    private void genHitboxes() {
        //for every square, generate a bounding box for it
        for (int i = 0; i < NUM_SQUARES; i++) {
            for (int j = 0; j < NUM_SQUARES; j++) {
                BoardPos pos = new BoardPos(i, j);
                squareHitboxes.put(pos, new BoundingBox(boardPosToWorld(pos).sub(SQUARE_WIDTH / 2, HEIGHT_OFFSET, SQUARE_WIDTH / 2),
                        boardPosToWorld(pos).add(SQUARE_WIDTH / 2, -HEIGHT_OFFSET, SQUARE_WIDTH / 2)));
            }
        }
    }

}
