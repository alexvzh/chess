package me.alex.piece.pieces;

import me.alex.board.Board;
import me.alex.move.Move;
import me.alex.piece.Piece;
import me.alex.piece.PieceColor;
import me.alex.board.Square;
import me.alex.scene.scenes.GameScene;

import java.util.HashMap;

public class Knight extends Piece {

    public Knight(Square square, PieceColor type, GameScene scene, Board board) {
        super(square, type, scene, board);
        setImageIndex(3);
    }

    @Override
    public void findLegalMoves() {

        HashMap<Square, Move> moveMap = new HashMap<>();
        int squareSize = board.getSquareSize();
        Square currentSquare = board.getSquare(getX(), getY());
        int x = (int) currentSquare.getBoundingBox().getX();
        int y = (int) currentSquare.getBoundingBox().getY();

        addMove(currentSquare, board.getSquare(x + squareSize * 2, y + squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x + squareSize * 2, y - squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x + squareSize, y + squareSize * 2), moveMap);
        addMove(currentSquare, board.getSquare(x + squareSize, y - squareSize * 2), moveMap);

        addMove(currentSquare, board.getSquare(x - squareSize * 2, y + squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x - squareSize * 2, y - squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x - squareSize, y + squareSize * 2), moveMap);
        addMove(currentSquare, board.getSquare(x - squareSize, y - squareSize * 2), moveMap);

        setMoveMap(moveMap);

    }

}
