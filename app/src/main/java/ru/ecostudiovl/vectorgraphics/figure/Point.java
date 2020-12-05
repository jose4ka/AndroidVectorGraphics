package ru.ecostudiovl.vectorgraphics.figure;

public class Point {

    private float x, y;
    private int parentFigureIndex;
    private int queueIndex;
    private boolean isSelected;

    public Point(float x, float y, int index, int queueIndex){
        this.x = x;
        this.y = y;
        this.parentFigureIndex = index;
        this.queueIndex = queueIndex;
        this.isSelected = false;
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

    public int getParentFigureIndex() {
        return parentFigureIndex;
    }

    public void setParentFigureIndex(int parentFigureIndex) {
        this.parentFigureIndex = parentFigureIndex;
    }

    public int getQueueIndex() {
        return queueIndex;
    }

    public void setQueueIndex(int queueIndex) {
        this.queueIndex = queueIndex;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
