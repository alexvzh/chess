package me.alex.piece.pieces;

import me.alex.board.Board;
import me.alex.move.Move;
import me.alex.move.MoveType;
import me.alex.piece.Piece;
import me.alex.piece.PieceColor;
import me.alex.board.Square;
import me.alex.scene.scenes.GameScene;

import java.util.HashMap;

public class King extends Piece {

    private boolean hasMovedBefore = false;

    public King(Square square, PieceColor type, GameScene scene, Board board) {
        super(square, type, scene, board);
        setImageIndex(0);
    }

    @Override
    public void findLegalMoves() {

        HashMap<Square, Move> moveMap = new HashMap<>();
        int squareSize = board.getSquareSize();
        Square currentSquare = board.getSquare(getX(), getY());

        addMove(currentSquare, board.getSquare(x + squareSize, y), moveMap);
        addMove(currentSquare, board.getSquare(x + squareSize, y + squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x + squareSize, y - squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x - squareSize, y), moveMap);
        addMove(currentSquare, board.getSquare(x - squareSize, y + squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x - squareSize, y - squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x, y + squareSize), moveMap);
        addMove(currentSquare, board.getSquare(x, y - squareSize), moveMap);

        addShortCastle(currentSquare, moveMap);
        addLongCastle(currentSquare, moveMap);

        setMoveMap(moveMap);

    }

    private void addShortCastle(Square currentSquare, HashMap<Square, Move> moveMap) {
        if (hasMovedBefore) return;
        Square closeRightSquare = board.getSquare(currentSquare.getBoundingBox().x + board.getSquareSize(), currentSquare.getBoundingBox().y);
        Square farRightSquare = board.getSquare(currentSquare.getBoundingBox().x + board.getSquareSize()*2, currentSquare.getBoundingBox().y);
        Square rookSquare = board.getSquare(currentSquare.getBoundingBox().x + board.getSquareSize()*3, currentSquare.getBoundingBox().y);
        Rook rightRook = (Rook)rookSquare.getPiece();

        if (closeRightSquare.getPiece() != null || farRightSquare.getPiece() != null) return;
        if (isSquareAttacked(closeRightSquare) || isSquareAttacked(farRightSquare)) return;
        if (rightRook == null || rightRook.hasMovedBefore()) return;

        addMove(currentSquare, farRightSquare, moveMap, MoveType.SHORT_CASTLE);

    }

    private void addLongCastle(Square currentSquare, HashMap<Square, Move> moveMap) {
        if (hasMovedBefore) return;
        Square closeLeftSquare = board.getSquare(currentSquare.getBoundingBox().x - board.getSquareSize(), currentSquare.getBoundingBox().y);
        Square middleLeftSquare = board.getSquare(currentSquare.getBoundingBox().x - board.getSquareSize()*2, currentSquare.getBoundingBox().y);
        Square farLeftSquare = board.getSquare(currentSquare.getBoundingBox().x - board.getSquareSize()*3, currentSquare.getBoundingBox().y);
        Square rookSquare = board.getSquare(currentSquare.getBoundingBox().x - board.getSquareSize()*4, currentSquare.getBoundingBox().y);
        Rook leftRook = (Rook)rookSquare.getPiece();

        if (closeLeftSquare.getPiece() != null || middleLeftSquare.getPiece() != null || farLeftSquare.getPiece() != null) return;
        if (isSquareAttacked(closeLeftSquare) || isSquareAttacked(middleLeftSquare) || isSquareAttacked(farLeftSquare)) return;
        if (leftRook == null || leftRook.hasMovedBefore()) return;

        addMove(currentSquare, middleLeftSquare, moveMap, MoveType.LONG_CASTLE);

    }

    private boolean isSquareAttacked(Square square) {

        for (Piece piece : scene.getPieceHandler().getPieces()) {
            if (piece.getType() == type) continue;
            if (piece instanceof King) continue;
            piece.findLegalMoves();
            if (piece.getMoveMap().containsKey(square)) return true;
        }

        return false;
    }


    public boolean hasMovedBefore() {
        return hasMovedBefore;
    }

    public void setMovedBefore(boolean hasMovedBefore) {
        this.hasMovedBefore = hasMovedBefore;
    }
}
