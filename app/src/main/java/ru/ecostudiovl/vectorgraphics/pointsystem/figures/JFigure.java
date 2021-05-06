package ru.ecostudiovl.vectorgraphics.pointsystem.figures;

import java.util.LinkedList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class JFigure {

    private static final String TAG = "=== FIGURE";

    private boolean isSelected;
    private String name;
    private int templateIndex;
    private float centerX, centerY;
    protected List<Integer> points;

    private float leftX, leftY;
    private float rightX, rightY;

    public JFigure(String name, int templateIndex){
        this.points = new LinkedList<>();
        this.isSelected = false;
        this.name = name;
        this.centerX = 0;
        this.centerY = 0;
        this.leftX = 0;
        this.leftY = 0;
        this.rightX = 0;
        this.rightY = 0;
        this.templateIndex = templateIndex;
    }

    public void addPoint(int index, float x, float y, JFigureTemplates template){
        if (template.isClosePointNumber()){
            if (points.size() < template.getPointsCount()){
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



    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
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

    public int getTemplateIndex() {
        return templateIndex;
    }

    public void setTemplateIndex(int templateIndex) {
        this.templateIndex = templateIndex;
    }

    public float getLeftX() {
        return leftX;
    }

    public void setLeftX(float leftX) {
        this.leftX = leftX;
    }

    public float getLeftY() {
        return leftY;
    }

    public void setLeftY(float leftY) {
        this.leftY = leftY;
    }

    public float getRightX() {
        return rightX;
    }

    public void setRightX(float rightX) {
        this.rightX = rightX;
    }

    public float getRightY() {
        return rightY;
    }

    public void setRightY(float rightY) {
        this.rightY = rightY;
    }
}
