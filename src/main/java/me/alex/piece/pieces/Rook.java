package me.alex.piece.pieces;

import me.alex.board.Board;
import me.alex.move.Move;
import me.alex.piece.Piece;
import me.alex.piece.PieceColor;
import me.alex.board.Square;
import me.alex.scene.scenes.GameScene;

import java.util.HashMap;

public class Rook extends Piece {

    private boolean hasMovedBefore = false;

    public Rook(Square square, PieceColor type, GameScene scene, Board board) {
        super(square, type, scene, board);
        setImageIndex(4);
    }

    @Override
    public void findLegalMoves() {

        HashMap<Square, Move> moveMap = new HashMap<>();
        Square currentSquare = board.getSquare(getX(), getY());

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        addLinearMoves(currentSquare, moveMap, directions);
        setMoveMap(moveMap);

    }


    public boolean hasMovedBefore() {
        return hasMovedBefore;
    }

    public void setMovedBefore(boolean hasMovedBefore) {
        this.hasMovedBefore = hasMovedBefore;
    }
}
