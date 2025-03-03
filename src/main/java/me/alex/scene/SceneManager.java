package me.alex.scene;

import me.alex.scene.scenes.GameScene;

import javax.swing.*;
import java.util.ArrayList;

public class SceneManager {

    private static SceneManager instance;

    private final JFrame frame;
    private final ArrayList<Scene> scenes;

    private Scene currentScene;


    private SceneManager(JFrame frame) {

        this.frame = frame;
        this.scenes = new ArrayList<>();

        initScenes();

        this.currentScene = getScene(SceneID.GAME);
    }

    public void setScene(SceneID sceneID) {
        Scene newScene = getScene(sceneID);
        frame.remove(currentScene);
        frame.add(newScene);
        frame.pack();
        currentScene = newScene;
    }

    public Scene getScene(SceneID sceneID) {
        for (Scene scene : scenes) {
            if (scene.getID() == sceneID)
                return scene;
        }
        throw new RuntimeException("No scene found for " + sceneID);
    }

    private void initScenes() {
        scenes.add(new GameScene());
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public static SceneManager getInstance(JFrame frame) {
        if (instance == null)
            instance = new SceneManager(frame);
        return instance;
    }

}
