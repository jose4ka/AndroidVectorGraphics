package ru.ecostudiovl.vectorgraphics.figure;



public class Point implements Comparable<Point> {

    private float x, y;
    private Integer index;
    private boolean isSelected;

    public Point(float x, float y, int index){
        this.x = x;
        this.y = y;
        this.isSelected = false;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", index=" + index +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public int compareTo(Point o) {
        return this.index.compareTo(o.getIndex());
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
