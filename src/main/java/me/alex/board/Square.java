package me.alex.board;

import me.alex.piece.Piece;

import java.awt.*;

public class Square {

    private final int x;
    private final int y;
    private int size;
    private Color color;
    private Piece piece;
    private boolean isHighlighted;
    private boolean isCapturable;
    private boolean isHovered;

    public Square(int x, int y, int size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public void draw(Graphics2D g2d) {

        g2d.setColor(color);
        g2d.fillRect(x, y, size, size);

        g2d.setStroke(new BasicStroke(10));

        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.drawRect(x + 5, y + 5, (int) (size/1.1), (int) (size/1.1));

        if (isHighlighted) {
            int circleSize = size / 3;
            g2d.fillOval(x - circleSize / 2 + size / 2, y - circleSize / 2 + size / 2, circleSize, circleSize);
        }

        if (isCapturable) {
            g2d.drawOval(x + 5, y + 5, (int) (size/1.1), (int) (size/1.1));
        }

        if (isHovered) {
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.drawRect(x + 5, y + 5, (int) (size/1.1), (int) (size/1.1));
        }

    }

    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, size, size);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (this.piece != null) this.piece.setSquare(this);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public boolean isCapturable() {
        return isCapturable;
    }

    public void setHighlighted(boolean highlighted) {
        this.isHighlighted = highlighted;
    }

    public void setCapturable(boolean capturable) {
        this.isCapturable = capturable;
    }

    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }
}
