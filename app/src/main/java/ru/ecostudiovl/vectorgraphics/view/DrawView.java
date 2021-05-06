package ru.ecostudiovl.vectorgraphics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import ru.ecostudiovl.vectorgraphics.fragment.work.FragmentDrawer;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPoint;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public static String TAG = "=ABOBA= DRAW_VIEW"; //Тег для логов.
    public FragmentDrawer.Mode mode;//Текущий режим работы с экраном.

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
        mode = FragmentDrawer.Mode.view;
        selectedFigure = -1; //Изначально -1, т.к. фигур никаких нет
        touchedPoint = -1; //Изначально -1, т.к. точек никаких нет
        isPointTouched = false;
    }


    //При создании самого класса мы создаём сам поток, который будет рисовать
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
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
        if (JPointData.getInstance().getFigures().size() > 0){
            switch (mode) {
                case create:

                    if (selectedFigure != -1) {
                        int template = JPointData.getInstance().getFigures().get(selectedFigure).getTemplateIndex();
                        int pointsCount = JPointData.getInstance().getFigures().get(selectedFigure).getPoints().size();
                        if (JPointData.getInstance().getTemplates().get(template).isClosePointNumber()) {
                            if (pointsCount < JPointData.getInstance().getTemplates().get(template).getPointsCount()) {
                                JPointData.getInstance().getPoints().add(new JPoint(x, y, selectedFigure)); //Добавляем точку в общий список с точками
                                JPointData.getInstance().getFigures().get(selectedFigure).addPoint(JPointData.getInstance().getPoints().size() - 1,x, y, JPointData.getInstance().getTemplates().get(JPointData.getInstance().getFigures().get(selectedFigure).getTemplateIndex()));
                            }
                        } else {
                            JPointData.getInstance().getPoints().add(new JPoint(x, y, selectedFigure)); //Добавляем точку в общий список с точками
                            JPointData.getInstance().getFigures().get(selectedFigure).addPoint(JPointData.getInstance().getPoints().size() - 1,x , y, JPointData.getInstance().getTemplates().get(JPointData.getInstance().getFigures().get(selectedFigure).getTemplateIndex())); /*Добавляем
                        индекс точки в структуру данных*/
                        }
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

                        if (JPointData.getInstance().getPoints().size() > 0) {
                            if (JPointData.getInstance().getFigures().get(selectedFigure).isContainsPoint(touchedPoint)){
                                if (touchedPoint == -1) {
                                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                        touchedPoint = getTouchedPoint(x, y);
                                    }

                                } else {
                                    JPointData.getInstance().getPoints().get(touchedPoint).setX(x);
                                    JPointData.getInstance().getPoints().get(touchedPoint).setY(y);
                                }
                            }

                        }
                    }



                    return true;

                case delete:
                    if (selectedFigure != -1){
                        touchedPoint = getTouchedPoint(x, y);
                        if (touchedPoint != -1){
                            JPointData.getInstance().getPoints().remove(touchedPoint); //Удаляем точку из общего списка
                            JPointData.getInstance().getFigures().get(selectedFigure).deletePoint(touchedPoint); //Удаляем точку из текущей фигуры
                            minimize(touchedPoint); //Минимизируем все точки в структуре данных
                        }
                    }

                    return false;

                case view:
                    findFigureByLine(x, y);
                    return false;

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

        for (int i = 0; i < JPointData.getInstance().getPoints().size(); i++) {

            boolean pointInRadius = (
                    Math.pow((JPointData.getInstance().getPoints().get(i).getX() - centerX), 2)+
                            Math.pow(((JPointData.getInstance().getPoints().get(i).getY() - centerY)),2)) <
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
        for (int i = 0; i < JPointData.getInstance().getFigures().size(); i++) {
            for (int j = 0; j < JPointData.getInstance().getFigures().get(i).getPoints().size(); j++) {
                if ( JPointData.getInstance().getFigures().get(i).getPoints().get(j) > index){
                    JPointData.getInstance().getFigures().get(i).getPoints().set(j, JPointData.getInstance().getFigures().get(i).getPoints().get(j) - 1);
                }
            }
        }
    }

    public void deletePointsWithFigure(int figureIndex){
        for (int i = 0; i < JPointData.getInstance().getFigures().get(figureIndex).getPoints().size() ; i++) {
            JPointData.getInstance().getPoints().remove(JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(i).intValue());
            minimize(JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(i).intValue());
        }
    }

    private float getLength(float startX, float startY, float endX, float endY){
        return (float) Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2));
    }

    private float getDegrees(float aX, float aY, float bX, float bY){


        double angle = Math.toDegrees(Math.atan2(bX - aX, bY - aY));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;

        return (float) angle;
    }


    private int findNearPoint(float startX, float startY){
        float minLength = 1000000;
        int index = 0;
        boolean isFinded = false;
        for (int i = 0; i < JPointData.getInstance().getPoints().size(); i++) {
            float length = getLength(startX, startY,
                    JPointData.getInstance().getPoints().get(i).getX(),
                    JPointData.getInstance().getPoints().get(i).getY());

            if (length <= minLength){
                minLength = length;
                index = i;
                isFinded = true;
            }
        }



        if (isFinded){
            Log.i(TAG, "findNearPoint length : "+index + " l = "+minLength);
//            Log.i(TAG, "findNearPoint degrees : "+getDegrees(
//                    startX,
//                    startY,
//                    JPointData.getInstance().getPoints().get(index).getX(),
//                    JPointData.getInstance().getPoints().get(index).getY()));
            return index;
        }
        else {
            Log.i(TAG, "findNearPoint: POINTS NOT FOUND");
            return  -1;
        }

    }

    private void findFigureByLine(float x, float y){
        int pointIndex = findNearPoint(x, y);
        float acceptZone = 20;
        if (pointIndex != -1){
            int figureIndex = JPointData.getInstance().getPoints().get(pointIndex).getFigureIndex();
            int localPointIndex = JPointData.getInstance().getFigures().get(figureIndex).getLocalIndexOfPoint(pointIndex);

            JPoint sourcePoint = JPointData.getInstance().getPoints().get(
                    JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(localPointIndex)
            );

            float currPDegrees = getDegrees(sourcePoint.getX(), sourcePoint.getY(), x, y);

            JPoint nextPoint, prevPoint;
            if (localPointIndex == 0){

                Log.i(TAG, "findFigureByLine: loc point index = 0");
                nextPoint = JPointData.getInstance().getPoints().get(
                        JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(localPointIndex + 1)
                );
                float nextPDegrees = getDegrees(sourcePoint.getX(), sourcePoint.getY(), nextPoint.getX(), nextPoint.getY());



                if ((currPDegrees <= nextPDegrees + acceptZone) && (currPDegrees >= nextPDegrees - acceptZone)){
                    Log.i(TAG, "findFigureByLine: FIGURE FINDED "+figureIndex);
                }

            }
            else if(localPointIndex == JPointData.getInstance().getFigures().get(figureIndex).getPoints().size() - 1){

                Log.i(TAG, "findFigureByLine: loc point index = "+(JPointData.getInstance().getFigures().get(figureIndex).getPoints().size() - 1));

                prevPoint = JPointData.getInstance().getPoints().get(
                        JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(localPointIndex - 1)
                );
                float prevPDegrees = getDegrees(sourcePoint.getX(), sourcePoint.getY(), prevPoint.getX(), prevPoint.getY());

                if ((currPDegrees <= prevPDegrees + acceptZone) && (currPDegrees >= prevPDegrees - acceptZone)){
                    Log.i(TAG, "findFigureByLine: FIGURE FINDED "+figureIndex);
                }
            }
            else {
                Log.i(TAG, "findFigureByLine: loc point btw");
                nextPoint = JPointData.getInstance().getPoints().get(
                        JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(localPointIndex + 1)
                );
                float nextPDegrees = getDegrees(sourcePoint.getX(), sourcePoint.getY(), nextPoint.getX(), nextPoint.getY());

                prevPoint = JPointData.getInstance().getPoints().get(
                        JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(localPointIndex - 1)
                );
                float prevPDegrees = getDegrees(sourcePoint.getX(), sourcePoint.getY(), prevPoint.getX(), prevPoint.getY());

                if ((currPDegrees <= nextPDegrees + acceptZone) && (currPDegrees >= nextPDegrees - acceptZone)){
                    Log.i(TAG, "findFigureByLine: FIGURE FINDED "+figureIndex);
                }
                else if ((currPDegrees <= prevPDegrees + acceptZone) && (currPDegrees >= prevPDegrees - acceptZone)){
                    Log.i(TAG, "findFigureByLine: FIGURE FINDED "+figureIndex);
                }
            }

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

}
