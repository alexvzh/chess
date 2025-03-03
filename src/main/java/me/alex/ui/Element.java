package me.alex.ui;

public abstract class Element {

    protected float x;
    protected float y;
    protected RenderPriority renderPriority;

    public Element(float x, float y, RenderPriority renderPriority, ElementHandler elementHandler) {
        this.x = x;
        this.y = y;
        this.renderPriority = renderPriority;
        elementHandler.addElement(this);
    }

    public Element(float x, float y, ElementHandler elementHandler) {
        this(x, y, RenderPriority.MEDIUM, elementHandler);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public RenderPriority getRenderPriority() {
        return renderPriority;
    }

    public void setRenderPriority(RenderPriority renderPriority) {
        this.renderPriority = renderPriority;
    }
}
