package ru.ecostudiovl.vectorgraphics.pointsystem;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

            Log.i("=== COPY", "copyFigureToPosition: ==================");
            float lX = getLengthByOneAxis(figures.get(figure).getCenterX(),points.get(figures.get(figure).getPoints().get(i)).getX());
            float lY = getLengthByOneAxis(figures.get(figure).getCenterY(),points.get(figures.get(figure).getPoints().get(i)).getY());
            Log.i("=== COPY", "copyFigureToPosition: LENGTH "+lX + " : "+lY);


            PointDirection xDir = findHorizontalDirection(figures.get(figure).getCenterX(),points.get(figures.get(figure).getPoints().get(i)).getX());
            PointDirection yDir = findVerticalDirection(figures.get(figure).getCenterY(),points.get(figures.get(figure).getPoints().get(i)).getY());

            Log.i("=== COPY", "copyFigureToPosition: DIRECTIONS "+xDir + " : "+yDir);

            Log.i("=== COPY", "copyFigureToPosition: OLD CENTER "+ figures.get(figure).getCenterX() + " : "+ figures.get(figure).getCenterY());

            figures.get(figures.size() - 1).setCenterX(posX);
            figures.get(figures.size() - 1).setCenterY(posY);
            Log.i("=== COPY", "copyFigureToPosition: NEW CENTER "+posX + " : "+posY);
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

            Log.i("=== COPY", "copyFigureToPosition: NEW POS "+newPosX + " : "+newPosY);
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
