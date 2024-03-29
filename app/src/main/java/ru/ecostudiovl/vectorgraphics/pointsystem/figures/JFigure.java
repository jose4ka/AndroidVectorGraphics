package ru.ecostudiovl.vectorgraphics.pointsystem.figures;

import java.util.LinkedList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class JFigure {

    private String name; //Название фигуры
    private int templateIndex; //Индекс используемого шаблона
    private float centerX; //Центр фигуры по X
    private float centerY; //Центр фигуры по Y

    private float scale = 1; //Размер фигуры
    private float rotate = 0; //Поворот фигуры

    private float minX, maxX; //Крайние точки по X
    private float minY, maxY; //Крайние точки по Y
    protected List<Integer> points; //Индексы точек, которая хранит в себе фигура

    //Конструктор фигуры
    public JFigure(String name, int templateIndex){
        this.points = new LinkedList<>();
        this.name = name;
        this.centerX = 0;
        this.centerY = 0;
        this.templateIndex = templateIndex;
    }

    /*
    Процедура добавляющая точку в фигуру
    Сначала мы проверяем по шаблону, можем ли мы её добавить
    Затем добавляем её в список
    Добавляется именно индекс самой точки из общего списка точек
    На вход подаётся индекс точки, и шаблон на который мы ссылаемся
     */
    public void addPoint(int index, JFigureTemplates template){
        if (template.isClosePointNumber()){
            if (points.size() < template.getPointsCount()){
                points.add(index);
               // recalculateCenterByPointIndex(index);
                recalculateCenter();
            }
        }
        else {
            points.add(index);
     //       recalculateCenterByPointIndex(index);
            recalculateCenter();
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

    /*
    Процедура просчитывает центр масс фигуры на основе всех имеющихся точек
    у этой фигуры
    Ищется самая центральная точка, относительно самых крайних точек фигуры
     */
    public void recalculateCenter(){
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
            }
        }

    }

    /*
    Процедура удаляет точку из списка точек в фигуре
    На вход подаётся индекс точки из обшего списка точек
     */
    public void deletePoint(int index){
        if (index != -1){
            points.remove(getLocalIndexOfPoint(index));
        }

    }

    /*
    Процедура возвращает индекс точки в локальном списке фигуры
    На основе индекса из общего списка
     */
    public int getLocalIndexOfPoint(int index){
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).equals(index)){
                return i;
            }
        }
        return -1;
    }

    /*
    Процедура возвращающая булевое значение
    Говорящее о том, хранится точка в фигуре или нет
    На вход подаётся индекс из общего списка точек
     */
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
}
