package ru.ecostudiovl.vectorgraphics;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.JPoint;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.Multiplex;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public static String TAG = "=== DRAW_VIEW"; //Тег для логов.
    public MainActivity.Mode mode;//Текущий режим работы с экраном.

    public JPointData jPointData; /*Структура данных, которая хранит в себе данные о фигурах, основываясь
                                    на индексы из общего списка точек.*/

    private List<JPoint> points;/*Список абсолютно всех точек, которые есть на экране и которые задействуются
                                    в структуре данных, описывающих фигуры.*/

    private int selectedFigure; //Выбранная фигура на данный момент.
    private int touchedPoint; //Индекс точки, на которую мы тыкнули пальцем.
    private boolean isPointTouched; //Переменная нужна при перемещении точки, чтобы не цеплять точки которые очень рядом.

    /*
    TODO: реализовать нормальное перемещение по холсту, и масштабирование
    Вспомогательные перменные которые на данный момент не используются.
    Нужны для перемещения по холсту, и для масштабирования изображения.
     */
    private int scaleMultiplier = 1;
    private float xDelta, yDelta = 0;

    private DrawThread drawThread;//Главный поток, отвечающий за постоянную отрисовку фигур и точек (рендеринг).




    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
        initializeVariables();
    }

    private void initializeVariables(){
        mode = MainActivity.Mode.view;
        jPointData = new JPointData();
        points = new LinkedList<>();
        selectedFigure = -1; //Изначально -1, т.к. фигур никаких нет
        touchedPoint = -1; //Изначально -1, т.к. точек никаких нет
        isPointTouched = false;
    }


    //При создании самого класса мы создаём сам поток, который будет рисовать
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), this);
        startDrawThread();
    }

    //Останавливаем поток
    public void stopDrawThread() {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void startDrawThread() {
        //Устанавливаем переменуую как true, это нужно для управления потоком
        drawThread.setRunning(true);
        //Стартуем поток
        drawThread.start();
    }


    /*
    Эвент реагирующий на нажатия по экрану
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = (event.getX());
        float y = (event.getY());

        //Если у нас есть хоть какие-то фигуры, значит уже можем что-то делать с точками
        if (jPointData.getFigures().size() > 0){
            switch (mode) {
                case create:

                    if (selectedFigure != -1){
                        points.add(new JPoint(x, y)); //Добавляем точку в общий список с точками
                        jPointData.getFigures().get(selectedFigure).addPoint(points.size() - 1); /*Добавляем
                        индекс точки в структуру данных*/

                    }

                    return false;
                case edit:

                    if (selectedFigure != -1){

                        if (!isPointTouched){
                        touchedPoint = getTouchedPoint(x, y);
                        }

                        if (touchedPoint != -1){
                        isPointTouched = true;
                        }

                        //Если мы подняли палец, значит никакая точка не выбрана
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            isPointTouched = false;
                            touchedPoint = -1;
                        }

                        if (points.size() > 0) {
                            if (jPointData.getFigures().get(selectedFigure).isContainsPoint(touchedPoint)){
                                if (touchedPoint == -1) {
                                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                        touchedPoint = getTouchedPoint(x, y);
                                    }

                                } else {
                                    points.get(touchedPoint).setX(x);
                                    points.get(touchedPoint).setY(y);
                                }
                            }

                        }
                    }



                    return true;

                case delete:
                    if (selectedFigure != -1){
                        touchedPoint = getTouchedPoint(x, y);
                        if (touchedPoint != -1){
                            points.remove(touchedPoint); //Удаляем точку из общего списка
                            jPointData.getFigures().get(selectedFigure).deletePoint(touchedPoint); //Удаляем точку из текущей фигуры
                            minimize(touchedPoint); //Минимизируем все точки в структуре данных
                        }
                    }

                    return false;

                case view:
                    return true;

            }

        }


        return false;

    }

    /*
    Проверяем нахождение точки в радиусе
     */
    private int getTouchedPoint(float centerX, float centerY) {
        int result = -1;
        float radius = 30;

        for (int i = 0; i < points.size(); i++) {

            boolean pointInRadius = (
                    Math.pow((points.get(i).getX() - centerX), 2)+
                            Math.pow(((points.get(i).getY() - centerY)),2)) <
                    (Math.pow(radius, 2));

            if (pointInRadius) {
                result = i;
            }
        }
        return result;
    }


    /*
    Данная процедура минимизирует индексы точек которые ссылаются
    Для чего это нужно?
    Когда мы удаляем точку из общего списка, все точки после неё автоматически скатываются по индексу вниз
    Поэтому, чтобы не было несостыковок в структуре данных, мы в каждой фигуре,
    где есть индексы точек большие чем удалённая точка - понижаем эти индексы
     */
    private void minimize(int index){
        for (int i = 0; i < jPointData.getFigures().size(); i++) {
            for (int j = 0; j < jPointData.getFigures().get(i).getPoints().size(); j++) {
                if ( jPointData.getFigures().get(i).getPoints().get(j) > index){
                    jPointData.getFigures().get(i).getPoints().set(j, jPointData.getFigures().get(i).getPoints().get(j) - 1);
                }
            }
        }
    }

    public void deletePointsWithFigure(int figureIndex){
        for (int i = 0; i < jPointData.getFigures().get(figureIndex).getPoints().size() ; i++) {
            points.remove(jPointData.getFigures().get(figureIndex).getPoints().get(i).intValue());
            minimize(jPointData.getFigures().get(figureIndex).getPoints().get(i).intValue());
        }
    }




    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public int getSelectedFigure() {
        return selectedFigure;
    }

    public void setSelectedFigure(int selectedFigure) {
        this.selectedFigure = selectedFigure;
    }

    public int getScaleMultiplier() {
        return scaleMultiplier;
    }

    public void setScaleMultiplier(int scaleMultiplier) {
        this.scaleMultiplier = scaleMultiplier;
    }

    public float getxDelta() {
        return xDelta;
    }

    public void setxDelta(float xDelta) {
        this.xDelta = xDelta;
    }

    public float getyDelta() {
        return yDelta;
    }

    public void setyDelta(float yDelta) {
        this.yDelta = yDelta;}

    public JPointData getjPointData() {
        return jPointData;
    }

    public void setjPointData(JPointData jPointData) {
        this.jPointData = jPointData;
    }

    public List<JPoint> getPoints() {
        return points;
    }

    public void setPoints(List<JPoint> points) {
        this.points = points;
    }
}
