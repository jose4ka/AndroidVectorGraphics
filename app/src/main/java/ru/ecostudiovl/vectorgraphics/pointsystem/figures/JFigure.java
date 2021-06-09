package ru.ecostudiovl.vectorgraphics.pointsystem.figures;

import android.util.Log;

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

    private float scale = 1;
    private float rotate = 0;
    private boolean canScale = true;

    private float minX, maxX;
    private float minY, maxY;
    protected List<Integer> points;
    private float minRadius = 100000;


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
                points.add(index);
               // recalculateCenterByPointIndex(index);
                recalculateCenterTest();
            }
        }
        else {
            points.add(index);
     //       recalculateCenterByPointIndex(index);
            recalculateCenterTest();
        }

    }

    /*
    public void recalculateCenterByPointIndex(int index){
        JPoint currentPoint = JPointData.getInstance().getPoints().get(index);
        if (points.size() == 1){
            minX = currentPoint.getX();
            maxX = currentPoint.getX();
            minY = currentPoint.getY();
            maxY = currentPoint.getY();
            centerX = currentPoint.getX();
            centerY = currentPoint.getY();
        }
        else {
            if(currentPoint.getX() < minX){
                minX = currentPoint.getX();
            }
            if(currentPoint.getX() > maxX) {
                maxX = currentPoint.getX();
            }

            if(currentPoint.getY() < minY){
                minY = currentPoint.getY();
            }
            if(currentPoint.getY() > maxY){
                maxY = currentPoint.getY();
            }

            centerX = (minX + maxX) / 2;
            centerY = (minY + maxY) / 2;
        }


    }

     */

    public void recalculateCenterTest(){
        minRadius = 100000;
        if (points.size() == 1){

            minX = JPointData.getInstance().getPoints().get(points.get(0)).getX();
            maxX = JPointData.getInstance().getPoints().get(points.get(0)).getX();
            minY = JPointData.getInstance().getPoints().get(points.get(0)).getY();
            maxY = JPointData.getInstance().getPoints().get(points.get(0)).getY();
            centerX = JPointData.getInstance().getPoints().get(points.get(0)).getX();
            centerY = JPointData.getInstance().getPoints().get(points.get(0)).getY();

        }
        else {

            minX = JPointData.getInstance().getPoints().get(points.get(0)).getX();
            maxX = JPointData.getInstance().getPoints().get(points.get(0)).getX();
            minY = JPointData.getInstance().getPoints().get(points.get(0)).getY();
            maxY = JPointData.getInstance().getPoints().get(points.get(0)).getY();
            centerX = JPointData.getInstance().getPoints().get(points.get(0)).getX();
            centerY = JPointData.getInstance().getPoints().get(points.get(0)).getY();

            for (int i = 0; i < points.size(); i++) {


                JPoint currentPoint = JPointData.getInstance().getPoints().get(points.get(i));

                if(currentPoint.getX() < minX){
                    minX = currentPoint.getX();
                }
                if(currentPoint.getX() > maxX) {
                    maxX = currentPoint.getX();
                }


                if(currentPoint.getY() < minY){
                    minY = currentPoint.getY();
                }
                if(currentPoint.getY() > maxY){
                    maxY = currentPoint.getY();
                }

                centerX = (minX + maxX) / 2;
                centerY = (minY + maxY) / 2;

                if (points.size() > 1){
                    Log.i("=== FIGURE ", "recalculateCenterTest: CHECK SIZE");
                    float lRadius = getLength(centerX, centerY, currentPoint.getX(), currentPoint.getY());
                    Log.i("=== FIGURE ", "recalculateCenterTest: CURR RADIUS = "+minRadius);
                    Log.i("=== FIGURE ", "recalculateCenterTest: LRADIUS = "+lRadius);
                    if (lRadius != 0 && lRadius < minRadius){
                        minRadius = lRadius;
                    }
                }

            }
        }

        Log.i("=== FIGURE", "recalculateCenterTest: MIN RADIUS "+minRadius);

    }

    //Процедура находит блину отрезка между двумя точками
    private float getLength(float startX, float startY, float endX, float endY){
        return (float) Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2));
    }

    public void deletePoint(int index){
        if (index != -1){
            points.remove(getLocalIndexOfPoint(index));
        }

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

    public void setTemplateIndex(int templateIndex) {
        this.templateIndex = templateIndex;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public boolean isCanScale() {
        return canScale;
    }

    public void setCanScale(boolean canScale) {
        this.canScale = canScale;
    }

    public float getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(float minRadius) {
        this.minRadius = minRadius;
    }
}
