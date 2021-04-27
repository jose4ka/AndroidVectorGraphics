package ru.ecostudiovl.vectorgraphics.pointsystem.figures;

import java.util.LinkedList;
import java.util.List;

public class JFigure {

    private static final String TAG = "=== FIGURE";
    private int pointsCount;
    private boolean isClosePointNumber;
    private boolean isClosedFigure;
    private boolean isSelected;
    private String name;
    protected List<Integer> points;

    public JFigure(boolean isClosePointNumber, boolean isClosedFigure, int pointsCount, String name){
        this.pointsCount = pointsCount;
        this.points = new LinkedList<>();
        this.isClosePointNumber = isClosePointNumber;
        this.isClosedFigure = isClosedFigure;
        this.isSelected = false;
        this.name = name;
    }

    public void addPoint(int index){
        if (isClosePointNumber){
            if (points.size() < pointsCount){
                points.add(index);
            }
        }
        else {
            points.add(index);
        }
    }

    public void deletePoint(int index){
        points.remove(getLocalIndexOfPoint(index));
    }

    /*
    Нужно найти индекс точки в локальном списке
     */
    public int getLocalIndexOfPoint(int index){
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).equals(index)){
                return i;
            }
        }
        return -1;
    }


    public boolean isContainsPoint(int index){
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).equals(index)){
                return true;
            }
        }
        return false;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    public boolean isClosePointNumber() {
        return isClosePointNumber;
    }

    public void setClosePointNumber(boolean closePointNumber) {
        isClosePointNumber = closePointNumber;
    }

    public boolean isClosedFigure() {
        return isClosedFigure;
    }

    public void setClosedFigure(boolean closedFigure) {
        isClosedFigure = closedFigure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
