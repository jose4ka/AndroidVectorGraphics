package ru.ecostudiovl.vectorgraphics.pointsystem.figures;

import java.util.ArrayList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.JPair;

public class JFigure {

    private int pointsCount;
    private boolean isClosePointNumber;
    protected List<JPair> points;

    public JFigure(boolean isClosePointNumber, int pointsCount){
        this.pointsCount = pointsCount;
        this.points = new ArrayList<>();
        this.isClosePointNumber = isClosePointNumber;
    }

    public void addPair(int startIndex, int endIndex){
        if (isClosePointNumber){
            if (points.size() < pointsCount){
                points.add(new JPair(startIndex, endIndex));
            }
        }
        else {
            points.add(new JPair(startIndex, endIndex));
        }
    }

    public void deletePoint(int index){
        if (isContainsPoint(index)){
            int position = getIndexOfPoint(index);
            if (position != -1){
                if (position == points.size() -1){
                    points.get(position - 1).setEndIndex(points.get(position - 1).getStartIndex());
                }
                else if(position == 0){
                    points.get(position + 1).setStartIndex(points.get(position + 1).getEndIndex());
                }
                else {
                    points.get(position - 1).setEndIndex(points.get(position + 1).getStartIndex());
                    points.get(position + 1).setStartIndex(points.get(position - 1).getEndIndex());
                }

                points.remove(position);
            }


        }
    }

    public boolean isContainsPoint(int index){
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getStartIndex() == index ||
                    points.get(i).getEndIndex() == index){
                return true;
            }
        }
        return false;
    }

    public int getIndexOfPoint(int point){
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getStartIndex() == point ||
                    points.get(i).getEndIndex() == point){
                return i;
            }
        }
        return -1;
    }



    public void printData(){
        for (int i = 0; i < pointsCount; i++) {
            System.out.print(points.get(i).getStartIndex());
            if (i < pointsCount - 1){
                System.out.print("-");
            }

        }
        System.out.println();
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
    }

    public List<JPair> getPoints() {
        return points;
    }

    public void setPoints(List<JPair> points) {
        this.points = points;
    }
}
