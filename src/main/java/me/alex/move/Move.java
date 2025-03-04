package me.alex.move;

import me.alex.piece.Piece;
import me.alex.piece.PieceType;
import me.alex.piece.PieceColor;
import me.alex.board.Board;
import me.alex.board.Square;
import me.alex.piece.pieces.*;
import me.alex.scene.scenes.GameScene;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Move {

    private final int[] direction;
    private final Piece pieceToMove;
    private final Piece pieceToCapture;
    private final Board board;
    private final Square initialSquare;
    private final Square newSquare;
    private final MoveType moveType;
    private final GameScene scene;

    public Move(Piece pieceToMove, Square initialSquare, Square newSquare, GameScene scene, MoveType moveType, int[] direction) {
        this.scene = scene;
        this.board = Board.getInstance(scene);
        this.moveType = moveType;
        this.direction = direction;
        this.newSquare = newSquare;
        this.pieceToMove = pieceToMove;
        this.initialSquare = initialSquare;
        this.pieceToCapture = newSquare.getPiece();
    }

    public Move(Piece pieceToMove, Square initialSquare, Square newSquare, GameScene scene, int[] direction) {
        this(pieceToMove, initialSquare, newSquare, scene, MoveType.REGULAR, direction);
    }

    public Move(Piece pieceToMove, Square initialSquare, Square newSquare, GameScene scene, MoveType moveType) {
        this(pieceToMove, initialSquare, newSquare, scene, moveType, null);
    }

    public Move(Piece pieceToMove, Square initialSquare, Square newSquare, GameScene scene) {
        this(pieceToMove, initialSquare, newSquare, scene, MoveType.REGULAR, null);
    }

    public void doMove() {

        updatePieces();
        movePiece();

        if (moveType.equals(MoveType.EN_PASSANT)) removePawn();
        else if (moveType.equals(MoveType.SHORT_CASTLE)) shortCastle();
        else if (moveType.equals(MoveType.LONG_CASTLE)) longCastle();

        playSound();
        transformPawn(PieceType.QUEEN);

    }

    public void undoMove() {

    }

    public void updatePieces() {
        if (pieceToMove instanceof King && !((King) pieceToMove).hasMovedBefore()) ((King) pieceToMove).setMovedBefore(true);
        if (pieceToMove instanceof Rook && !((Rook) pieceToMove).hasMovedBefore()) ((Rook) pieceToMove).setMovedBefore(true);

        if (pieceToMove instanceof Pawn && !((Pawn) pieceToMove).hasMovedBefore()) {
            ((Pawn) pieceToMove).setMovedBefore(true);
            ((Pawn) pieceToMove).setMovedLastTurn(true);
        }

        for (Piece piece : scene.getPieceHandler().getPieces()) {
            if (piece instanceof Pawn && piece != pieceToMove && ((Pawn) piece).isHasMovedBefore())
                ((Pawn) piece).setMovedLastTurn(false);
        }
    }

    public void playSound() {
        if (moveType.equals(MoveType.CAPTURE) || moveType.equals(MoveType.EN_PASSANT)) playSound("/sounds/capture.wav");
        else if (moveType.equals(MoveType.SHORT_CASTLE) || moveType.equals(MoveType.LONG_CASTLE)) playSound("/sounds/castle.wav");
        else playSound("/sounds/move-self.wav");
    }

    private void movePiece() {
        if (moveType.equals(MoveType.CAPTURE)) scene.getPieceHandler().removePiece(newSquare.getPiece());
        pieceToMove.setX(newSquare.getBoundingBox().x);
        pieceToMove.setY(newSquare.getBoundingBox().y);
        newSquare.setPiece(pieceToMove);
        initialSquare.setPiece(null);
    }

    private void removePawn() {
        final PieceColor pieceColor = pieceToMove.getType();
        final int yOffset = pieceColor.equals(PieceColor.WHITE) ? board.getSquareSize() : -board.getSquareSize();
        final Square pieceSquare = board.getSquare(pieceToMove.getX(), pieceToMove.getY() + yOffset);
        pieceSquare.setPiece(null);
        scene.getPieceHandler().removePiece(pieceSquare.getPiece());
    }

    private void shortCastle() {
        final Square rookSquare = board.getSquare(pieceToMove.getX() + board.getSquareSize(), pieceToMove.getY());
        final Square newRookSquare = board.getSquare(pieceToMove.getX() - board.getSquareSize(), pieceToMove.getY());
        final Rook rightRook = (Rook) rookSquare.getPiece();
        rookSquare.setPiece(null);
        newRookSquare.setPiece(rightRook);
        rightRook.setX(newRookSquare.getBoundingBox().x);
        rightRook.setY(newRookSquare.getBoundingBox().y);
    }

    private void longCastle() {
        final Square rookSquare = board.getSquare(pieceToMove.getX() - board.getSquareSize()*2, pieceToMove.getY());
        final Square newRookSquare = board.getSquare(pieceToMove.getX() + board.getSquareSize(), pieceToMove.getY());
        final Rook leftRook = (Rook) rookSquare.getPiece();
        rookSquare.setPiece(null);
        newRookSquare.setPiece(leftRook);
        leftRook.setX(newRookSquare.getBoundingBox().x);
        leftRook.setY(newRookSquare.getBoundingBox().y);
    }

    private void transformPawn(PieceType pieceType) {

        if (!(pieceToMove instanceof Pawn)) return;
        final PieceColor type = pieceToMove.getType();

        if (type.equals(PieceColor.BLACK) && newSquare.getBoundingBox().y != board.getSquareSize() * 7
                || type.equals(PieceColor.WHITE) && newSquare.getBoundingBox().y != 0) return;

        Piece newPiece;
        switch (pieceType) {
            case ROOK:
                newPiece = new Rook(newSquare, type, scene, board);
                break;
            case BISHOP:
                newPiece = new Bishop(newSquare, type, scene, board);
                break;
            case KNIGHT:
                newPiece = new Knight(newSquare, type, scene, board);
                break;
            default:
                newPiece = new Queen(newSquare, type, scene, board);
                break;
        }

        scene.getPieceHandler().removePiece(pieceToMove);
        newSquare.setPiece(newPiece);

    }

    public void playSound(final String url) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            getClass().getResourceAsStream(url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public MoveType getType() {
        return moveType;
    }

    public Square getNewSquare() {
        return newSquare;
    }

    public int[] getDirection() {
        return direction;
    }

    public Piece getPieceToCapture() {
        return pieceToCapture;
    }
}
