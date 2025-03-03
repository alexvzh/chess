package me.alex.move;

import me.alex.piece.Piece;
import me.alex.piece.PieceColor;
import me.alex.board.Board;
import me.alex.board.Square;
import me.alex.piece.pieces.King;
import me.alex.scene.scenes.GameScene;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoveManager implements java.awt.event.MouseListener, MouseMotionListener {

    private final GameScene scene;
    private final Board board;
    private Square initialSquare;
    private Square oldSquare;
    private Piece currentPiece;
    private ArrayList<Move> moves;
    private Move currentMove;
    private boolean currentTurnInCheck;

    public MoveManager(GameScene scene) {
        this.scene = scene;
        this.board = Board.getInstance(scene);
        this.moves = new ArrayList<>();
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (board.viewingOldPosition()) return;

        Point p = e.getPoint();

        currentPiece = scene.getPieceHandler().getPiece(p);
        if (currentPiece == null) return;
        if (!currentPiece.getType().equals(board.getTurn())) return;

        moveToFront(currentPiece);
        if (currentPiece.getMoveMap() == null && !currentTurnInCheck) currentPiece.findLegalMoves();

        initialSquare = board.getSquare(p);
        currentPiece.followMouse(true);

        for (Move move : currentPiece.getMoveMap().values()) {
            Square square = move.getNewSquare();
            if (move.getType().equals(MoveType.CAPTURE) || move.getType().equals(MoveType.EN_PASSANT))
                square.setCapturable(true);
            else
                square.setHighlighted(true);
        }

        if (oldSquare != initialSquare) {
            if (oldSquare != null) oldSquare.setHovered(false);
            oldSquare = initialSquare;
            initialSquare.setHovered(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (board.viewingOldPosition()) return;

        if (currentPiece == null) return;
        if (!currentPiece.getType().equals(board.getTurn())) return;

        final Point p = e.getPoint();
        final Square newSquare = board.getSquare(p);

        currentPiece.followMouse(false);
        board.resetHighlight();
        moveToSquare(initialSquare, newSquare);
        oldSquare.setHovered(false);
        oldSquare = null;
        currentPiece = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (board.viewingOldPosition()) return;

        if (currentPiece == null) return;
        if (!currentPiece.getType().equals(board.getTurn())) return;

        Point p = e.getPoint();
        Square square = board.getSquare(p);

        if (square == null) return;
        if (oldSquare != square) {
            if (oldSquare != null) oldSquare.setHovered(false);
            oldSquare = square;
            square.setHovered(true);
        }
    }

    private void moveToSquare(Square initialSquare, Square newSquare) {

        Move move = currentPiece.getMoveMap().get(newSquare);
        if (move == null) {
            cancelMove(initialSquare);
            return;
        }

        move.doMove();

        changeTurn();
        board.addCurrentPosition();

    }

    private void cancelMove(Square initialSquare) {
        currentPiece.setX(initialSquare.getBoundingBox().x);
        currentPiece.setY(initialSquare.getBoundingBox().y);
    }

    private void changeTurn() {
        currentTurnInCheck = false;
        PieceColor lastTurn = board.getTurn();

        board.setTurn(lastTurn.equals(PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE);

        for (Piece piece : scene.getPieceHandler().getPieces()) {
            piece.setMoveMap(null);
        }

        postMove(lastTurn);

    }

    private void postMove(PieceColor lastTurn) {
        HashMap<Piece, Move> attackingMoves = new HashMap<>();
        ArrayList<Square> attackingSquares = new ArrayList<>();

        for (Piece piece : scene.getPieceHandler().getPieces()) {
            if (!piece.getType().equals(lastTurn)) continue;
            piece.findLegalMoves();

            for (Move move : piece.getMoveMap().values()) {

                if (move.getType().equals(MoveType.CAPTURE) && move.getPieceToCapture() instanceof King) {

                    attackingMoves.put(piece, move);
                    attackingSquares.add(piece.getSquare());
                    currentTurnInCheck = true;
                    break;
                }
            }
        }

        if (!currentTurnInCheck) return;

        for (Map.Entry<Piece, Move> entry : attackingMoves.entrySet()) {
            Piece piece = entry.getKey();
            Move move = entry.getValue();

            if (move.getDirection() == null) continue;
            for (Move m : piece.getMoveMap().values()) {
                if (m.getDirection() == move.getDirection()) {
                    attackingSquares.add(m.getNewSquare());
                }
            }
        }

        for (Piece piece : scene.getPieceHandler().getPieces()) {
            if (!piece.getType().equals(board.getTurn())) continue;
            HashMap<Square, Move> allowedMoves = new HashMap<>();
            piece.findLegalMoves();
            for (Square square : piece.getMoveMap().keySet()) {
                if (attackingSquares.contains(square)) {
                    allowedMoves.put(square, piece.getMoveMap().get(square));
                }
            }
            piece.setMoveMap(allowedMoves);
        }
    }

    private void moveToFront(Piece piece) {
        scene.getPieceHandler().removePiece(piece);
        scene.getPieceHandler().addPiece(piece);
    }

    @Override
    public void mouseClicked(MouseEvent e) throws UnsupportedOperationException {}

    @Override
    public void mouseEntered(MouseEvent e) throws UnsupportedOperationException {}

    @Override
    public void mouseExited(MouseEvent e) throws UnsupportedOperationException {}

    @Override
    public void mouseMoved(MouseEvent e)  throws UnsupportedOperationException {
        mouseDragged(e);
    }

}
