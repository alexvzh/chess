package me.alex.scene;

import me.alex.piece.PieceHandler;

import javax.swing.*;
import java.awt.*;

public abstract class Scene extends JPanel implements Runnable {

    private boolean running;
    private SceneID sceneID;

    protected final PieceHandler pieceHandler;

    protected Scene() {

        this.setPreferredSize(new Dimension(1400, 800));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setLayout(null);

        this.pieceHandler = new PieceHandler();

        startThread();

    }

    public void startThread() {
        new Thread(this).start();
        running = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        draw(g2d);
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2d);

    @Override
    public void run() {
        final int FPS = 1024;
        final double TIME_PER_UPDATE = 1_000_000_000.0 / FPS;

        long lastUpdateTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastUpdateTime) / TIME_PER_UPDATE;
            lastUpdateTime = currentTime;

            while (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public PieceHandler getPieceHandler() {
        return pieceHandler;
    }

    public SceneID getID() {
        return sceneID;
    }

    public void setSceneID(SceneID sceneID) {
        this.sceneID = sceneID;
    }
}
