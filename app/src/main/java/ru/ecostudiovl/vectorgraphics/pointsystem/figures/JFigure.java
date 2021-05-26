package ru.ecostudiovl.vectorgraphics.pointsystem.figures;

import java.util.LinkedList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.JPoint;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class JFigure {

    private String name;
    private int templateIndex;
    private float centerX;
    private float centerY;
    protected List<Integer> points;


    public JFigure(String name, int templateIndex){
        this.points = new LinkedList<>();
        this.name = name;
        this.centerX = 0;
        this.centerY = 0;
        this.templateIndex = templateIndex;
    }

    public void addPoint(int index, JFigureTemplates template){
        if (template.isClosePointNumber()){
            if (points.size() < template.getPointsCount()){
                smartAdd(index);
            }
        }
        else {
            smartAdd(index);
        }
    }

    private void smartAdd(int index){
        points.add(index);
        if(points.size() == 1){
            centerX = JPointData.getInstance().getPoints().get(index).getX();
            centerY = JPointData.getInstance().getPoints().get(index).getY();
        }
        recalculateCenterByIndex(index);
    }

    public void recalculateCenterByIndex(int pointIndex){
        JPoint jPoint = JPointData.getInstance().getPoints().get(pointIndex);

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



    public List<Integer> getPoints() {
        return points;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemplateIndex() {
        return templateIndex;
    }

}
