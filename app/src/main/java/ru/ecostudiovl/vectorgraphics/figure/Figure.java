package ru.ecostudiovl.vectorgraphics.figure;

import java.util.ArrayList;
import java.util.List;

public class Figure {

    private List<Point> points;
    private int figuresListIndex;
    private float size;

    private float centerXMin;
    private float centerXMax;

    private float centerYMin;
    private float centerYMax;

    private float centerX;
    private float centerY;

    private boolean isSelected;
    private boolean hasSelectedPoint;
    private String name;

    public Figure(int figuresListIndex, String name){
        this.points = new ArrayList<>();
        this.figuresListIndex = figuresListIndex;
        this.size = 1;

        this.centerXMin = 0;
        this.centerXMax = 0;

        this.centerYMin = 0;
        this.centerYMax = 0;

        this.centerX = 0;
        this.centerY = 0;

        this.isSelected = false;
        this.hasSelectedPoint = false;
        this.name = name;
    }

    public void addPoint(float x, float y){
        points.add(new Point(x, y, figuresListIndex, points.size()));
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

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasSelectedPoint() {
        return hasSelectedPoint;
    }

    public void setHasSelectedPoint(boolean hasSelectedPoint) {
        this.hasSelectedPoint = hasSelectedPoint;
    }
}
