package ru.ecostudiovl.vectorgraphics.pointsystem;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.math.*;

import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class JPointData {


    private List<JFigure> figures;
    private List<JFigureTemplates> templates;

    private List<JPoint> points;/*Список абсолютно всех точек, которые есть на экране и которые задействуются
                                    в структуре данных, описывающих фигуры.*/


    private static JPointData instance;

    private enum PointDirection{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }


    private JPointData(){
        figures = new ArrayList<>();
        templates = new ArrayList<>();
        templates.add(new JFigureTemplates(0, false, false, "Кривая"));
        templates.add(new JFigureTemplates(0, false, true, "Замкнутая фигура"));
        templates.add(new JFigureTemplates(3, true, true, "Треугольник"));
        templates.add(new JFigureTemplates(4, true, true, "Четырёхугольник"));
        templates.add(new JFigureTemplates(5, true, true, "Пятиугольник"));
        templates.add(new JFigureTemplates(6, true, true, "Шестиугольник"));
        templates.add(new JFigureTemplates(3, true, false, "Угол"));
        points = new LinkedList<>();
    }

    public static JPointData getInstance(){
        if (instance == null) {
            instance = new JPointData();
        }

        return instance;
    }

    public void copyFigureToPosition(int figure, float posX, float posY){
        figures.add(new JFigure(figures.get(figure).getName() + " (copy)", figures.get(figure).getTemplateIndex()));

        for (int i = 0; i < figures.get(figure).getPoints().size(); i++) {

            float lX = getLengthByOneAxis(figures.get(figure).getCenterX(),points.get(figures.get(figure).getPoints().get(i)).getX());
            float lY = getLengthByOneAxis(figures.get(figure).getCenterY(),points.get(figures.get(figure).getPoints().get(i)).getY());

            PointDirection xDir = findHorizontalDirection(figures.get(figure).getCenterX(),points.get(figures.get(figure).getPoints().get(i)).getX());
            PointDirection yDir = findVerticalDirection(figures.get(figure).getCenterY(),points.get(figures.get(figure).getPoints().get(i)).getY());

            figures.get(figures.size() - 1).setCenterX(posX);
            figures.get(figures.size() - 1).setCenterY(posY);

            float newPosX = 0;
            float newPosY = 0;

            switch (xDir){
                case LEFT:
                    newPosX = posX - lX;
                    break;
                case RIGHT:
                    newPosX = posX + lX;
                    break;
            }

            switch (yDir){
                case UP:
                    newPosY = posY - lY;
                    break;
                case DOWN:
                    newPosY = posY + lY;
                    break;
            }

            points.add(new JPoint(newPosX,newPosY));
            figures.get(figures.size() - 1).addPoint(points.size() -1,
                    templates.get(figures.get(figures.size() - 1).getTemplateIndex())
            );
        }

    }


    public void moveFigureToPosition(int figure, float posX, float posY){
        for (int i = 0; i < figures.get(figure).getPoints().size(); i++) {

            float lX = getLengthByOneAxis(figures.get(figure).getCenterX(),points.get(figures.get(figure).getPoints().get(i)).getX());
            float lY = getLengthByOneAxis(figures.get(figure).getCenterY(),points.get(figures.get(figure).getPoints().get(i)).getY());

            PointDirection xDir = findHorizontalDirection(figures.get(figure).getCenterX(),points.get(figures.get(figure).getPoints().get(i)).getX());
            PointDirection yDir = findVerticalDirection(figures.get(figure).getCenterY(),points.get(figures.get(figure).getPoints().get(i)).getY());


            float newPosX = 0;
            float newPosY = 0;

            switch (xDir){
                case LEFT:
                    newPosX = posX - lX;
                    break;
                case RIGHT:
                    newPosX = posX + lX;
                    break;
            }

            switch (yDir){
                case UP:
                    newPosY = posY - lY;
                    break;
                case DOWN:
                    newPosY = posY + lY;
                    break;
            }

            points.get(figures.get(figure).getPoints().get(i)).setX(newPosX);
            points.get(figures.get(figure).getPoints().get(i)).setY(newPosY);
        }

        figures.get(figure).setCenterX(posX);
        figures.get(figure).setCenterY(posY);
    }


    public void rotateFigurePlus(int figure, float angle){
        JFigure jFigure = figures.get(figure);

        float s = (float) Math.sin(angle); // angle is in radians
        float c = (float) Math.cos(angle); // angle is in radians

        for (int i = 0; i < figures.get(figure).getPoints().size(); i++) {
            JPoint currPoint = points.get(figures.get(figure).getPoints().get(i));

            float xnew = currPoint.getX() * c + currPoint.getY() * s;
            float ynew = -currPoint.getX() * s + currPoint.getY() * c;

            points.get(figures.get(figure).getPoints().get(i)).setX(xnew);
            points.get(figures.get(figure).getPoints().get(i)).setY(ynew);
            //
//            points.get(figures.get(figure).getPoints().get(i)).setX((float) (jFigure.getCenterX() + (currPoint.getX() - jFigure.getCenterX()) * Math.cos(angle) - (currPoint.getY() - jFigure.getCenterY()) * Math.sin(angle)));
//
//            // Y = y0 + (y - y0) * cos(a) + (x - x0) * sin(a);
//            points.get(figures.get(figure).getPoints().get(i)).setY((float) (jFigure.getCenterY() + (currPoint.getY() - jFigure.getCenterY()) * Math.cos(angle) - (currPoint.getX() - jFigure.getCenterX()) * Math.sin(angle)));
        }



    }

    public void rotateFigureMinus(int figure, float angle){
        JFigure jFigure = figures.get(figure);

        float s = (float) Math.sin(angle); // angle is in radians
        float c = (float) Math.cos(angle); // angle is in radians

        for (int i = 0; i < figures.get(figure).getPoints().size(); i++) {
            JPoint currPoint = points.get(figures.get(figure).getPoints().get(i));

            float xnew = currPoint.getX() * c - currPoint.getY() * s;
            float ynew = currPoint.getX() * s + currPoint.getY() * c;

            points.get(figures.get(figure).getPoints().get(i)).setX(xnew);
            points.get(figures.get(figure).getPoints().get(i)).setY(ynew);
            //
//            points.get(figures.get(figure).getPoints().get(i)).setX((float) (jFigure.getCenterX() + (currPoint.getX() - jFigure.getCenterX()) * Math.cos(angle) - (currPoint.getY() - jFigure.getCenterY()) * Math.sin(angle)));
//
//            // Y = y0 + (y - y0) * cos(a) + (x - x0) * sin(a);
//            points.get(figures.get(figure).getPoints().get(i)).setY((float) (jFigure.getCenterY() + (currPoint.getY() - jFigure.getCenterY()) * Math.cos(angle) - (currPoint.getX() - jFigure.getCenterX()) * Math.sin(angle)));
        }



    }


    public PointDirection findHorizontalDirection(float sX, float eX){
        if (eX > sX){ return PointDirection.RIGHT;}
        else { return PointDirection.LEFT;}
    }

    public PointDirection findVerticalDirection(float sY, float eY){
        if (eY > sY){ return PointDirection.DOWN;}
        else { return PointDirection.UP;}
    }


    private float getLengthByOneAxis(float startX, float endX){
        return (float) Math.sqrt(Math.pow((endX - startX), 2));
    }

    private float getLength(float startX, float startY, float endX, float endY){
        return (float) Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2));
    }

    public List<JFigure> getFigures() {
        return figures;
    }


    public List<JFigureTemplates> getTemplates() {
        return templates;
    }


    public List<JPoint> getPoints() {
        return points;
    }


}
