package me.alex.piece.pieces;

import me.alex.board.Board;
import me.alex.move.Move;
import me.alex.piece.Piece;
import me.alex.piece.PieceColor;
import me.alex.board.Square;
import me.alex.scene.scenes.GameScene;

import java.util.HashMap;

public class Queen extends Piece {

    public Queen(Square square, PieceColor type, GameScene scene, Board board) {
        super(square, type, scene, board);
        setImageIndex(1);
    }

    @Override
    public void findLegalMoves() {

        HashMap<Square, Move> moveMap = new HashMap<>();
        Square currentSquare = board.getSquare(getX(), getY());

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        addLinearMoves(currentSquare, moveMap, directions);
        setMoveMap(moveMap);

    }

}
