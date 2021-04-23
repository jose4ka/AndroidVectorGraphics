package ru.ecostudiovl.vectorgraphics;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.JPair;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPoint;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.Multiplex;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public static String TAG = "=== DRAW_VIEW";
    public MainActivity.Mode mode = MainActivity.Mode.create;

    public JPointData jPointData;
    private List<JPoint> points;

    private DrawThread drawThread;

    private int selectedFigure = 0;

    private int catchPoint = -1;
    private boolean isPointCatched = false;

    private int scaleMultiplier = 1;
    private float xDelta, yDelta = 0;


    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
        jPointData = new JPointData();
        jPointData.getFigures().add(new Multiplex());
        points = new ArrayList<>();
        mode = MainActivity.Mode.create;
        isPointCatched = false;
    }


    //При создании самого класса мы создаём сам поток, который будет рисовать
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), this);
        startDrawThread();
    }

    public void stopDrawThread() {
        //Останавливаем поток

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


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = (event.getX());
        float y = (event.getY());

        if (jPointData.getFigures().size() > 0){
            switch (mode) {
                case create:

                    if (selectedFigure != -1){
                        points.add(new JPoint(x, y, points.size()));

                        if(points.size() == 1){
                            jPointData.getFigures().get(selectedFigure).addPair(0,0);
                        }
                        else{
                            jPointData.getFigures().get(selectedFigure).getPoints().set(0, new JPair(0, 1));
                            jPointData.getFigures().get(selectedFigure).addPair(

                                    jPointData.getFigures().get(selectedFigure).getPoints().get(
                                            jPointData.getFigures().get(selectedFigure).getPoints().size() - 1
                                    ).getEndIndex(),
                                    points.size() - 1);
                        }
                    }

                    return false;
                case edit:

                    if (!isPointCatched){
                        catchPoint = getTouchedPoint(x, y);
                    }
                    if (catchPoint != -1){
                        isPointCatched = true;
                    }
                    if (selectedFigure != -1){
                        //Если мы подняли палец, значит никакая точка не выбрана
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            isPointCatched = false;
                            catchPoint = -1;
                        }
                        if (points.size() > 0) {
                            if (catchPoint == -1) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    catchPoint = getTouchedPoint(x, y);
                                }

                            } else {
                                points.get(catchPoint).setX(x);
                                points.get(catchPoint).setY(y);
                            }
                        }
                    }



                    return true;

                case delete:

                    if (selectedFigure != -1){
                        catchPoint = getTouchedPoint(x, y);
                        if (catchPoint != -1){
                            points.remove(catchPoint);
                            jPointData.getFigures().get(selectedFigure).deletePoint(catchPoint);
                        }
                    }


                    return false;

                case view:
                    return true;

            }

        }


        return false;

    }

    private int getTouchedPoint(float x, float y) {
        int result = -1;

        for (int i = 0; i < points.size(); i++) {
            if (Math.abs(points.get(i).getX() - x) <= 30 &&
                    (Math.abs(points.get(i).getY() - y) <= 30)) {
                result = i;
            }
        }
        return result;
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
