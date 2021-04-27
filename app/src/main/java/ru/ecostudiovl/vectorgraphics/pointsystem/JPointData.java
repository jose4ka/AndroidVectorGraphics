package ru.ecostudiovl.vectorgraphics.pointsystem;

import java.util.ArrayList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;

public class JPointData {


    private List<JFigure> figures;

    public JPointData(){
        figures = new ArrayList<>();
    }

    public float findLength(JPoint start, JPoint end){
        return (float) Math.sqrt(Math.pow((end.getX() - start.getX()), 2)+Math.pow((end.getY() - start.getY()), 2));
    }

    public boolean comeInRadius(JPoint start, JPoint end, float r){
        return Math.pow((start.getX() - end.getX()),2) + Math.pow((start.getY() - end.getY()),2) < Math.pow(r, 2);
    }

    public List<JFigure> getFigures() {
        return figures;
    }

    public void setFigures(List<JFigure> figures) {
        this.figures = figures;
    }
}
