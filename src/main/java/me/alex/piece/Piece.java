package me.alex.piece;

import me.alex.move.Move;
import me.alex.board.Board;
import me.alex.board.Square;
import me.alex.move.MoveType;
import me.alex.scene.scenes.GameScene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Piece {

    private static final BufferedImage SPRITE_SHEET;

    static {
        try {
            SPRITE_SHEET = ImageIO.read(Objects.requireNonNull(Piece.class.getResourceAsStream("/images/caliente.png")));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    protected int x;
    protected int y;
    protected Board board;
    protected Square square;
    protected PieceColor type;
    protected GameScene scene;

    private BufferedImage image;
    private HashMap<Square, Move> moves;
    private int imageIndex;
    private boolean isSelected;

    protected Piece(Square square, PieceColor type, GameScene scene, Board board) {
        this.x = square.getBoundingBox().x;
        this.y = square.getBoundingBox().y;
        this.type = type;
        this.scene = scene;
        this.board = board;

        square.setPiece(this);

        scene.getPieceHandler().addPiece(this);

    }

    public abstract void findLegalMoves();

    public void update() {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation, scene);
        if (isSelected) {
            x = mouseLocation.x - board.getSquareSize()/2;
            y = mouseLocation.y - board.getSquareSize()/2;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x ,y, null);
    }

    private void loadImage(PieceColor type) {
        BufferedImage original;
        int initialSize = 512;
        int size = 100;

        if (type.equals(PieceColor.WHITE))
            original = SPRITE_SHEET.getSubimage(imageIndex * initialSize, 0, initialSize, initialSize);
        else
            original = SPRITE_SHEET.getSubimage(imageIndex * initialSize, initialSize, initialSize, initialSize);

        image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(original.getScaledInstance(size, size, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
        loadImage(type);
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, board.getSquareSize(), board.getSquareSize());
    }

    public void followMouse(boolean selected) {
        this.isSelected = selected;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PieceColor getType() {
        return type;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
        this.x = square.getBoundingBox().x;
        this.y = square.getBoundingBox().y;
    }

    public Map<Square, Move> getMoveMap() {
        return moves;
    }

    public void setMoveMap(HashMap<Square, Move> moves) {
        this.moves = moves;
    }

    public void addMove(Square currentSquare, Square newSquare, Map<Square, Move> moveMap) {
        if (newSquare == null) return;
        if (newSquare.getPiece() != null && newSquare.getPiece().getType().equals(this.getType())) return;
        if (newSquare.getPiece() != null && !newSquare.getPiece().getType().equals(this.getType())) {
            moveMap.put(newSquare, new Move(this, currentSquare, newSquare, scene, MoveType.CAPTURE));
            return;
        }

        moveMap.put(newSquare, new Move(this, currentSquare, newSquare, scene));
    }

    public void addMove(Square currentSquare, Square newSquare, Map<Square, Move> moveMap, MoveType moveType) {
        moveMap.put(newSquare, new Move(this, currentSquare, newSquare, scene, moveType));
    }

    public void addLinearMoves(Square currentSquare, Map<Square, Move> moveMap, int[][] directions) {
        for (int[] dir : directions) {

            int dx = dir[0];
            int dy = dir[1];

            for (int i = board.getSquareSize(); i < board.getSquareSize() * 8; i += board.getSquareSize()) {
                Square square = board.getSquare(x + dx * i, y + dy * i);
                if (addLinearMove(currentSquare, square, moveMap, dir)) break;
            }
        }
    }

    private boolean addLinearMove(Square currentSquare, Square newSquare, Map<Square, Move> moveMap, int[] direction) {
        if (newSquare == null) return true;
        if (newSquare.getPiece() != null && newSquare.getPiece().getType().equals(this.getType())) return true;
        if (newSquare.getPiece() != null && !newSquare.getPiece().getType().equals(this.getType())) {
            moveMap.put(newSquare, new Move(this, currentSquare, newSquare, scene, MoveType.CAPTURE, direction));
            return true;
        }

        moveMap.put(newSquare, new Move(this, currentSquare, newSquare, scene, direction));
        return false;
    }
}
