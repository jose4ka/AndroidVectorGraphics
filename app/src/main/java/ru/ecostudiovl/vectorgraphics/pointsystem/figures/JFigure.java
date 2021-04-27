package ru.ecostudiovl.vectorgraphics.pointsystem.figures;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.JPair;

public class JFigure {

    private static final String TAG = "=== FIGURE";
    private int pointsCount;
    private boolean isClosePointNumber;
    private boolean isClosedFigure;
    private String name;
    protected List<Integer> points;

    public JFigure(boolean isClosePointNumber, boolean isClosedFigure, int pointsCount, String name){
        this.pointsCount = pointsCount;
        this.points = new LinkedList<>();
        this.isClosePointNumber = isClosePointNumber;
        this.isClosedFigure = isClosedFigure;
        this.name = name;
    }

    public void addPoint(int index){
        Log.i(TAG, "addPoint: "+index);
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
        points.remove(getIndexOfPoint(index));
    }

    public int getIndexOfPoint(int index){
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).equals(index)){
                return i;
            }
        }
        return -1;
    }


    public void printEdges(){

//        for (int i = 0; i < edges.size(); i++) {
//            Log.i(TAG, "printEdges: "+edges.get(i).getStartIndex() + " - "+edges.get(i).getEndIndex());
//        }
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
}
