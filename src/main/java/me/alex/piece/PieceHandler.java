package me.alex.piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PieceHandler {

    private final ArrayList<Piece> pieces = new ArrayList<>();

    public void update() {
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).update();
        }
    }

    public void draw(Graphics2D g2d) {
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).draw(g2d);
        }
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public Piece getPiece(Point p) {
        for (Piece piece : pieces) {
            if (piece.getBoundingBox().contains(p))
                return piece;
        }
        return null;
    }

    public List<Piece> getPieces() {
        return pieces;
    }
}
