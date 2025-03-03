package me.alex.board;

import me.alex.piece.Piece;
import me.alex.piece.PieceColor;
import me.alex.piece.pieces.*;
import me.alex.scene.scenes.GameScene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private static Board instance;

    private PieceColor turn;
    private final ArrayList<Square> squares;
    private int squareSize;
    private Color firstColor;
    private Color secondColor;
    private final ArrayList<ArrayList<Piece>> positions;
    private final GameScene scene;

    private Board(int squareSize, Color firstColor, Color secondColor, GameScene scene) {

        this.squares = new ArrayList<>();
        this.squareSize = squareSize;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.turn = PieceColor.WHITE;
        this.positions = new ArrayList<>();
        this.scene = scene;

        initSquares();
        initPieces(squareSize);
        addCurrentPosition();

    }

    public void draw(Graphics2D g2d) {
      for (Square square : squares) {
          square.draw(g2d);
      }
    }

    private void initSquares() {
        int count = 0;
        int xOffset = 300;
        for (int i = xOffset; i < 8 * squareSize + xOffset; i += squareSize) {
            count++;
            for (int j = 0; j < 8 * squareSize; j += squareSize) {
                if (count % 2 == 0) squares.add(new Square(i, j, squareSize, firstColor));
                else squares.add(new Square(i, j, squareSize, secondColor));
                count++;
            }
        }
    }

    private void initPieces(int squareSize) {
        int xOffset = 300;

        for (int x = xOffset; x < squareSize * 8 + xOffset; x += squareSize) {
            new Pawn(getSquare(x, squareSize), PieceColor.BLACK, scene, this);
            new Pawn(getSquare(x, squareSize * 6), PieceColor.WHITE, scene, this);
        }

        new Rook(getSquare(xOffset, 0), PieceColor.BLACK, scene, this);
        new Rook(getSquare(xOffset, squareSize * 7), PieceColor.WHITE, scene, this);
        new Rook(getSquare(squareSize * 7 + xOffset, 0), PieceColor.BLACK, scene, this);
        new Rook(getSquare(squareSize * 7 + xOffset, squareSize * 7), PieceColor.WHITE, scene, this);

        new Knight(getSquare(squareSize + xOffset, 0), PieceColor.BLACK, scene, this);
        new Knight(getSquare(squareSize + xOffset, squareSize * 7), PieceColor.WHITE, scene, this);
        new Knight(getSquare(squareSize * 6 + xOffset, 0), PieceColor.BLACK, scene, this);
        new Knight(getSquare(squareSize * 6 + xOffset, squareSize * 7), PieceColor.WHITE, scene, this);

        new Bishop(getSquare(squareSize * 2 + xOffset, 0), PieceColor.BLACK, scene, this);
        new Bishop(getSquare(squareSize * 2 + xOffset, squareSize * 7), PieceColor.WHITE, scene, this);
        new Bishop(getSquare(squareSize * 5 + xOffset, 0), PieceColor.BLACK, scene, this);
        new Bishop(getSquare(squareSize * 5 + xOffset, squareSize * 7), PieceColor.WHITE, scene, this);

        new Queen(getSquare(squareSize * 3 + xOffset, 0), PieceColor.BLACK, scene, this);
        new Queen(getSquare(squareSize * 3 + xOffset, squareSize * 7), PieceColor.WHITE, scene, this);

        new King(getSquare(squareSize * 4 + xOffset, 0), PieceColor.BLACK, scene, this);
        new King(getSquare(squareSize * 4 + xOffset, squareSize * 7), PieceColor.WHITE, scene, this);

    }

    public int getSquareSize() {
        return squareSize;
    }

    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    public Color getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(Color firstColor) {
        this.firstColor = firstColor;
    }


    public Square getSquare(Point point) {

        for (Square square : squares) {
            if (square.getBoundingBox().contains(point)) return square;
        }
        return null;
    }

    public Square getSquare(int x, int y) {

        for (Square square : squares) {
            if (square.getBoundingBox().contains(new Point(x, y))) return square;
        }
        return null;
    }

    public void resetHighlight() {
        for (Square square : squares) {
            square.setHighlighted(false);
            square.setCapturable(false);
        }
    }

    public PieceColor getTurn() {
        return turn;
    }

    public void setTurn(PieceColor turn) {
        this.turn = turn;
    }

    public void loadPosition(int positionIndex) {
        ArrayList<Piece> position = positions.get(positionIndex);
        for (int i = 0; i < position.size(); i++) {
            squares.get(i).setPiece(position.get(i));
        }

        for (int i = 0; i < position.size(); i++) {
            if (position.get(i) == null) continue;
            position.get(i).setX(squares.get(i).getBoundingBox().x);
            position.get(i).setY(squares.get(i).getBoundingBox().y);
        }

    }

    public void addCurrentPosition() {
        ArrayList<Piece> position = new ArrayList<>();
        for (Square square : squares) {
            position.add(square.getPiece());
        }
        positions.add(position);
    }

    public List<Piece> getCurrentPosition() {
        ArrayList<Piece> position = new ArrayList<>();
        for (Square square : squares) {
            position.add(square.getPiece());
        }
        return position;
    }

    public boolean viewingOldPosition() {
        return !getCurrentPosition().equals(positions.get(positions.size() - 1));
    }

    public static Board getInstance(GameScene scene) {
        if (instance == null)
            instance = new Board(100, Color.GRAY, Color.LIGHT_GRAY, scene);
        return instance;
    }
}
