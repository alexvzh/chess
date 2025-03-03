package me.alex.scene.scenes;

import me.alex.move.MoveManager;
import me.alex.board.Board;
import me.alex.scene.Scene;
import me.alex.scene.SceneID;

import java.awt.*;

public class GameScene extends Scene {

    private final Board board;

    public GameScene() {

        this.board = Board.getInstance(this);
        MoveManager mouseListener = new MoveManager(this);

        this.setSceneID(SceneID.GAME);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);

    }

    @Override
    public void update() {
        pieceHandler.update();
    }

    @Override
    public void draw(Graphics2D g2d) {
        board.draw(g2d);
        pieceHandler.draw(g2d);
    }

}
