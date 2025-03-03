package me.alex.piece.pieces;

import me.alex.move.Move;
import me.alex.move.MoveType;
import me.alex.piece.Piece;
import me.alex.piece.PieceColor;
import me.alex.board.Board;
import me.alex.board.Square;
import me.alex.scene.scenes.GameScene;

import java.awt.*;
import java.util.HashMap;

public class Pawn extends Piece {

    private boolean hasMovedBefore = false;
    private boolean movedLastTurn = false;

    public Pawn(Square square, PieceColor type, GameScene scene, Board board) {
        super(square, type, scene, board);
        setImageIndex(5);
    }

    @Override
    public void findLegalMoves() {
        HashMap<Square, Move> moveMap = new HashMap<>();

        int yOffset = type.equals(PieceColor.WHITE) ? -board.getSquareSize() : board.getSquareSize();

        addBasicMoves(moveMap, yOffset, board);
        addDiagonalMoves(moveMap, yOffset, board);

        setMoveMap(moveMap);
    }

    public void addBasicMoves(HashMap<Square, Move> moveMap, int yOffset, Board board) {
        if (!hasMovedBefore && board.getSquare(new Point(x + board.getSquareSize() / 2, y + yOffset)).getPiece() == null) {
            Square newSquare =  board.getSquare(new Point(x + board.getSquareSize() / 2, y + 2 * yOffset));
            moveMap.put(newSquare, new Move(this, square, newSquare, scene));
        }
        if ((board.getSquare(new Point(x + board.getSquareSize() / 2, y + yOffset)).getPiece() == null)) {
            Square newSquare =  board.getSquare(new Point(x + board.getSquareSize() / 2, y + yOffset));
            moveMap.put(newSquare, new Move(this, square, newSquare, scene));
        }
    }

    public void addDiagonalMoves(HashMap<Square, Move> moveMap, int yOffset, Board board) {
        if (x + board.getSquareSize() < board.getSquareSize() * 8 + 300) {
            Square rightDiagonal = board.getSquare(x + board.getSquareSize(), y + yOffset);

            if (rightDiagonal.getPiece() != null && !rightDiagonal.getPiece().getType().equals(type)) {
                moveMap.put(rightDiagonal, new Move(this, square, rightDiagonal, scene, MoveType.CAPTURE));
            }

            Piece rightPiece = board.getSquare(x + board.getSquareSize(), y).getPiece();
            if (rightPiece != null && !rightPiece.getType().equals(type) && rightPiece instanceof Pawn && ((Pawn) rightPiece).isMovedLastTurn())
                moveMap.put(rightDiagonal, new Move(this, square, rightDiagonal, scene, MoveType.EN_PASSANT));
        }

        if (x - board.getSquareSize() >= 300) {
            Square leftDiagonal = board.getSquare(x - board.getSquareSize(), y + yOffset);

            if (leftDiagonal.getPiece() != null && !leftDiagonal.getPiece().getType().equals(type))
                moveMap.put(leftDiagonal, new Move(this, square, leftDiagonal, scene, MoveType.CAPTURE));

            Piece leftPiece = board.getSquare(x - board.getSquareSize(), y).getPiece();
            if (leftPiece != null && !leftPiece.getType().equals(type) && leftPiece instanceof Pawn && ((Pawn) leftPiece).isMovedLastTurn())
                moveMap.put(leftDiagonal, new Move(this, square, leftDiagonal, scene, MoveType.EN_PASSANT));
        }
    }

    public boolean hasMovedBefore() {
        return hasMovedBefore;
    }

    public void setMovedBefore(boolean hasMovedBefore) {
        this.hasMovedBefore = hasMovedBefore;
    }

    public boolean isMovedLastTurn() {
        return movedLastTurn;
    }

    public void setMovedLastTurn(boolean movedLastTurn) {
        this.movedLastTurn = movedLastTurn;
    }

    public boolean isHasMovedBefore() {
        return hasMovedBefore;
    }

    public void setHasMovedBefore(boolean hasMovedBefore) {
        this.hasMovedBefore = hasMovedBefore;
    }
}
