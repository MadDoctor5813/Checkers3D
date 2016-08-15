package com.kevin.scene;

import org.joml.Vector3f;

/**
 * Created by 532259 on 1/21/2016.
 */

//Represents those blue squares that highlight a piece and its moves
public class SelectionEntity extends Entity {

    public SelectionEntity(BoardEntity board, BoardPos boardPos) {
        super(board.scene, board.boardPosToWorld(boardPos).sub(0, board.HEIGHT_OFFSET, 0),
                new Vector3f(1), new Vector3f(0), "selection.asset");
    }

}
