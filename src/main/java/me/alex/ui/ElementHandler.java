package me.alex.ui;

import me.alex.ui.behaviour.Clickable;
import me.alex.ui.behaviour.Drawable;
import me.alex.ui.behaviour.HasBoundingBox;
import me.alex.ui.behaviour.Updatable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Comparator;

public class ElementHandler implements java.awt.event.MouseListener, MouseMotionListener {

    private final ArrayList<Element> elements;

    public ElementHandler() {
        this.elements = new ArrayList<>();
    }

    public void addElement(Element element) {
        elements.add(element);
        elements.sort(Comparator.comparing(Element::getRenderPriority));
    }

    public void removeElement(Element element) {
        elements.remove(element);
    }

    public void updateElements() {
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (!(element instanceof Updatable)) continue;
            ((Updatable)element).update();
        }
    }

    public void drawElements(Graphics2D g2d) {
        for (int i = 0; i < elements.size(); i++) {
            final Element element = elements.get(i);
            if (!(element instanceof Drawable)) continue;
            ((Drawable)element).draw(g2d);
        }
    }

    public void clickElement(Point p) {
        final Element element = getElementAt(p);
        if (element == null) return;
        ((Clickable) element).onClick();
    }

    public Element getElementAt(Point p) {
        for (Element element : elements) {
            if (!(element instanceof HasBoundingBox)) continue;
            if (((HasBoundingBox) element).getBoundingBox().contains(p)) return element;
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickElement(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
