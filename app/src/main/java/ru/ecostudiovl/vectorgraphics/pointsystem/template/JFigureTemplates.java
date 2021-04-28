package ru.ecostudiovl.vectorgraphics.pointsystem.template;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;

public class JFigureTemplates {

    private int pointsCount;
    private boolean isClosePointNumber;
    private boolean isClosedFigure;

    private List<JFigure> figures;

    public JFigureTemplates(int pointsCount, boolean isClosePointNumber, boolean isClosedFigure){
        figures = new ArrayList<>();
        this.pointsCount = pointsCount;
        this.isClosePointNumber = isClosePointNumber;
        this.isClosedFigure = isClosedFigure;
    }

    public List<JFigure> getFigures() {
        return figures;
    }

    public void setFigures(List<JFigure> figures) {
        this.figures = figures;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
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
}
