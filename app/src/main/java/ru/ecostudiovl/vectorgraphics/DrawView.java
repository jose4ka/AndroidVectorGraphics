package ru.ecostudiovl.vectorgraphics;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.figure.Figure;
import ru.ecostudiovl.vectorgraphics.figure.Point;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public MainActivity.Mode mode;

    public List<Figure> figures;

    private DrawThread drawThread;

    private int selectedFigure = -1;

    private int catchPoint = -1;


    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
        figures = new ArrayList<>();
        mode = MainActivity.Mode.create;
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


        if (figures.size() > 0){
            switch (mode) {
                case create:

                    if (selectedFigure != -1){
                        if (figures.get(selectedFigure).points.size() == 0) {
                            figures.get(selectedFigure).points.add(new Point(event.getX(), event.getY(), selectedFigure, figures.get(selectedFigure).points.size()));
                        } else if (figures.get(selectedFigure).points.size() >= 1) {
                            figures.get(selectedFigure).points.get(figures.get(selectedFigure).points.size() - 1).nextIndex = figures.get(selectedFigure).points.size();
                            figures.get(selectedFigure).points.add(new Point(event.getX(), event.getY(), selectedFigure, 0));
                        }
                    }




                    return false;
                case edit:

                    if (selectedFigure != -1){
                        //Если мы подняли палец, значит никакая точка не выбрана
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            catchPoint = -1;
                        }
                        if (figures.get(selectedFigure).points.size() > 0) {
                            if (catchPoint == -1) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    catchPoint = getTouchedPoint(event.getX(), event.getY());
                                }

                            } else {
                                figures.get(selectedFigure).points.get(catchPoint).x = event.getX();
                                figures.get(selectedFigure).points.get(catchPoint).y = event.getY();
                            }
                        }
                    }



                    return true;

                case delete:

                    if (selectedFigure != -1){
                        if (figures.get(selectedFigure).points.size() > 0) {
                            int touched = getTouchedPoint(event.getX(), event.getY());

                            if (touched == -1){
                                return false;
                            }
                            if (figures.get(selectedFigure).points.size() >= 1) {

                                if (touched == 0) { //Если это первая точка, то тогда у передней точки обновляем ссылку на минус 1
                                    for (int i = 0; i < figures.get(selectedFigure).points.size(); i++) {
                                        figures.get(selectedFigure).points.get(i).nextIndex -= 1;
                                    }
                                    figures.get(selectedFigure).points.get(figures.get(selectedFigure).points.size() - 1).nextIndex = 0;


                                } else if (touched == figures.get(selectedFigure).points.size() - 1) { //Последняя точка
                                    figures.get(selectedFigure).points.get(touched - 1).nextIndex = 0;

                                } else { //Иначе, если точка не нулевая, а где-то в середине - то обновляем ссылку предыдущей точки на следующую
                                    for (int i = touched + 1; i < figures.get(selectedFigure).points.size(); i++) {
                                        figures.get(selectedFigure).points.get(i).nextIndex -= 1;
                                    }
                                    figures.get(selectedFigure).points.get(figures.get(selectedFigure).points.size() - 1).nextIndex = 0;

                                }

                            }


                            figures.get(selectedFigure).points.remove(touched);
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

        for (int i = 0; i < figures.get(selectedFigure).points.size(); i++) {
            if (Math.abs(figures.get(selectedFigure).points.get(i).x - x) <= 30 &&
                    (Math.abs(figures.get(selectedFigure).points.get(i).y - y) <= 30)) {
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
}
