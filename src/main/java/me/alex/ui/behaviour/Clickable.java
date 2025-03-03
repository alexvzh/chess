package me.alex.ui.behaviour;

import java.awt.*;

public interface Clickable extends HasBoundingBox {
    void onClick();
    Rectangle getBounds();
}
