package ru.ecostudiovl.vectorgraphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import ru.ecostudiovl.vectorgraphics.figure.Figure;
import ru.ecostudiovl.vectorgraphics.figure.Point;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public MainActivity.Mode mode;

    //Массив 1 - x и y, массив 2 - номер фигуры и точки в ней
    public List<Point> points;

    private DrawThread drawThread;

    private int catchPoint = -1;


    public DrawView(Context context){
        super(context);
        getHolder().addCallback(this);
        Log.d("RENDER", "=== POINT 2");
        points = new ArrayList<>();
        mode = MainActivity.Mode.create;
    }


    //При создании самого класса мы создаём сам поток, который будет рисовать
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("RENDER", "=== POINT 3");
        drawThread = new DrawThread(getHolder(), this);
        startDrawThread();
    }

    public void stopDrawThread(){
        //Останавливаем поток

        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    public void startDrawThread(){
        Log.d("RENDER", "=== POINT 4");
        //Устанавливаем переменуую как true, это нужно для управления потоком
        drawThread.setRunning(true);
        //Стартуем поток
        drawThread.start();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (mode){
            case create:
                if (points.size() == 0){
                    points.add(new Point(event.getX(), event.getY(), points.size()));
                }
                else if (points.size() >= 1){
                    points.get(points.size() - 1).nextIndex = points.size();
                    points.add(new Point(event.getX(), event.getY(), 0));
                }

                return false;
            case edit:

                //Если мы подняли палец, значит никакая точка не выбрана
                if (event.getAction() == MotionEvent.ACTION_UP){
                    catchPoint = -1;
                }
                if (points.size() > 0){
                    if (catchPoint == -1){
                        if (event.getAction() == MotionEvent.ACTION_DOWN){
                            catchPoint = getTouchedPoint(event.getX(), event.getY());
                        }

                    }
                    else {
                        points.get(catchPoint).x = event.getX();
                        points.get(catchPoint).y = event.getY();
                    }

                }
                return true;

            case delete:
                if (points.size() > 0) {
                    int touched = getTouchedPoint(event.getX(), event.getY());
                    if (points.size() > 1) {

                        if (touched == 0) { //Если это первая точка, то тогда у передней точки обновляем ссылку на минус 1
                            for (int i = 0; i < points.size(); i++) {
                                points.get(i).nextIndex -= 1;
                            }
                            points.get(points.size() - 1).nextIndex = 0;
                        }
                        else if (touched == points.size() - 1) { //Последняя точка
                            points.get(touched - 1).nextIndex = 0;
                        } else { //Иначе, если точка не нулевая, а где-то в середине - то обновляем ссылку предыдущей точки на следующую
                            for (int i = touched + 1; i < points.size(); i++) {
                                points.get(i).nextIndex -= 1;
                            }
                            points.get(points.size() - 1).nextIndex = 0;
                        }

                    }
                    points.remove(touched);


                }


                return false;
        }



        return false;

    }

    private int getTouchedPoint(float x, float y){
        int result = -1;

        for (int i = 0; i < points.size(); i++) {
            if (Math.abs(points.get(i).x - x) <= 30 &&
                    (Math.abs(points.get(i).y - y) <= 30)){
                result = i;
            }
        }
        return result;
    }





    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }




}
