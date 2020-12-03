package ru.ecostudiovl.vectorgraphics.point;

import java.util.ArrayList;
import java.util.List;

public class Figure {

    private List<Point> points;
    private int figuresListIndex;
    private float size;

    public Figure(int figuresListIndex){
        this.points = new ArrayList<>();
        this.figuresListIndex = figuresListIndex;
        this.size = 1;
    }

    public void addPoint(float x, float y){
        points.add(new Point(x * size, y * size, figuresListIndex, points.size() - 1));
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public int getFiguresListIndex() {
        return figuresListIndex;
    }

    public void setFiguresListIndex(int figuresListIndex) {
        this.figuresListIndex = figuresListIndex;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
