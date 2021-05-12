package ru.ecostudiovl.vectorgraphics.pointsystem.template;

public class JFigureTemplates {

    private int pointsCount;
    private boolean isClosePointNumber;
    private boolean isClosedFigure;
    private String name;

    public JFigureTemplates(int pointsCount, boolean isClosePointNumber, boolean isClosedFigure, String name){
        this.pointsCount = pointsCount;
        this.isClosePointNumber = isClosePointNumber;
        this.isClosedFigure = isClosedFigure;
        this.name = name;


    }


    public int getPointsCount() {
        return pointsCount;
    }


    public boolean isClosePointNumber() {
        return isClosePointNumber;
    }

    public boolean isClosedFigure() {
        return isClosedFigure;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
