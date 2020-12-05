package ru.ecostudiovl.vectorgraphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import ru.ecostudiovl.vectorgraphics.figure.Figure;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public enum State{
        Overview,
        FigureCreation,
        FigureOperation,
        PointOperation
    }

    private State currentState;

    private List<Figure> figures;
    private int selectedFigure;

    private int touchedFigure, touchedPoint, lastSelFigure, lastSelPoint;

    //Массив 1 - x и y, массив 2 - номер фигуры и точки в ней
    private TreeMap<String, float[]> pointsMap;

    private Paint p = new Paint();

    private DrawThread drawThread;

    public DrawView(Context context){
        super(context);
        getHolder().addCallback(this);
        initialize();
    }

    private void initialize(){
        figures = new ArrayList<>();
        pointsMap = new TreeMap<>();
        selectedFigure = 0;
        touchedFigure = 0;
        touchedPoint = 0;
        lastSelFigure = 0;
        lastSelPoint = 0;
        currentState = State.Overview;
    }

    //При создании самого класса мы создаём сам поток, который будет рисовать
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        drawThread = new DrawThread(getHolder(), this);
        //Стартуем поток отрисовки графики
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
        //Устанавливаем переменуую как true, это нужно для управления потоком
        drawThread.setRunning(true);
        //Стартуем поток
        drawThread.start();
    }

    private boolean hasInteract(float x, float y){

        //Т.к. мы нажимаем на экран, то мы не можем нажать пиксель в пиксель
        float minXDelta = 20;
        float minYDelta = 20;

        for (TreeMap.Entry<String,float[] > entry: pointsMap.entrySet()) {
            if (Math.abs(x - entry.getValue()[0]) <= minXDelta && Math.abs(y - entry.getValue()[1]) <= minYDelta){
                String[] data = entry.getKey().split(":");
                touchedFigure = Integer.parseInt(data[0]);
                touchedPoint = Integer.parseInt(data[1]);
                return true;
            }
        }

        return false;
    }

    private boolean hasEditInteract(float x, float y){

        //Т.к. мы нажимаем на экран, то мы не можем нажать пиксель в пиксель
        int minXDelta = 30;
        int minYDelta = 30;

        for (TreeMap.Entry<String,float[] > entry: pointsMap.entrySet()) {
            if (Math.abs(x - entry.getValue()[0]) <= minXDelta && Math.abs(y - entry.getValue()[1]) <= minYDelta){
                if (Integer.parseInt(entry.getKey().split(":")[0]) == selectedFigure && !figures.get(selectedFigure).isHasSelectedPoint()){
                    touchedFigure = Integer.parseInt(entry.getKey().split(":")[0]);
                    touchedPoint = Integer.parseInt(entry.getKey().split(":")[1]);
                    return true;
                }
            }
        }

        return false;
    }

    private void disableAllPointsSelects(){
        for (HashMap.Entry<String, float[]> entry: pointsMap.entrySet()) {
            figures.get(Integer.parseInt(entry.getKey().split(":")[0])).getPoints().get(Integer.parseInt(entry.getKey().split(":")[1])).setSelected(false);
        }
    }

    private void disableAllFiguresSelects(){
        for (HashMap.Entry<String, float[]> entry: pointsMap.entrySet()) {
            figures.get(Integer.parseInt(entry.getKey().split(":")[0])).setSelected(false);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean result = true;

        float column = event.getX();
        float row = event.getY();

        Log.d("TOUCH", "col "+column + " : row "+row);

        switch (currentState){
            case Overview:
                if (hasInteract(column, row)){
                    disableAllPointsSelects();
                    figures.get(touchedFigure).getPoints().get(touchedPoint).setSelected(true);
                    disableAllFiguresSelects();
                    figures.get(touchedFigure).setSelected(true);
                    Log.d("OVERVIEW", touchedFigure +" - "+ touchedPoint);
                }
                else {
                    disableAllPointsSelects();
                    disableAllFiguresSelects();
                    Log.d("OVERVIEW", "NOT A POINT");
                }
                result = true;

                break;
            case FigureCreation:

                figures.get(selectedFigure).addPoint(column, row);
                Log.d("ADD", selectedFigure+" - "+(figures.get(selectedFigure).getPoints().size()-1));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(selectedFigure);
                stringBuilder.append(":");
                stringBuilder.append(figures.get(selectedFigure).getPoints().size() - 1);
                pointsMap.put(stringBuilder.toString(), new float[]{column, row});
                result = false;
                break;
            case PointOperation:
                if (event.getAction() == MotionEvent.ACTION_UP){
                    figures.get(selectedFigure).setHasSelectedPoint(false);
                    disableAllPointsSelects();
                    return true;
                }
                if (hasEditInteract(column, row)){
                    if (!figures.get(selectedFigure).isHasSelectedPoint()){
                        figures.get(selectedFigure).setHasSelectedPoint(true);
                        disableAllPointsSelects();
                        figures.get(selectedFigure).getPoints().get(touchedPoint).setSelected(true);
                        disableAllFiguresSelects();
                        figures.get(selectedFigure).setSelected(true);

                        StringBuilder stringBuilderOperatons = new StringBuilder();
                        stringBuilderOperatons.append(selectedFigure);
                        stringBuilderOperatons.append(":");
                        stringBuilderOperatons.append(touchedPoint);

                        pointsMap.put(stringBuilderOperatons.toString(), new float[]{column, row});

                        figures.get(selectedFigure).getPoints().get(touchedPoint).setX(column);
                        figures.get(selectedFigure).getPoints().get(touchedPoint).setY(row);
                    }

                }
                else {
                    if (figures.get(selectedFigure).isHasSelectedPoint()){
                        figures.get(selectedFigure).setHasSelectedPoint(false);
                        disableAllPointsSelects();
                    }


                }

                result = true;
                break;
            case FigureOperation:
                break;
        }


        return result;
    }



    public void addFigure(String name){
        disableAllFiguresSelects();
        disableAllPointsSelects();
        figures.add(new Figure(figures.size(), name));
        setSelectedFigure(figures.size() - 1);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }



    public void clear(){
        initialize();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public List<Figure> getFigures() {
        return figures;
    }

    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }

    public int getSelectedFigure() {
        return selectedFigure;
    }

    public void setSelectedFigure(int selectedFigure) {

        disableAllPointsSelects();
        disableAllFiguresSelects();
        figures.get(selectedFigure).setSelected(true);
        figures.get(selectedFigure).setHasSelectedPoint(false);
        this.selectedFigure = selectedFigure;
    }

    public int getTouchedFigure() {
        return touchedFigure;
    }

    public void setTouchedFigure(int touchedFigure) {
        this.touchedFigure = touchedFigure;
    }

    public int getTouchedPoint() {
        return touchedPoint;
    }

    public void setTouchedPoint(int touchedPoint) {
        this.touchedPoint = touchedPoint;
    }

    public Paint getP() {
        return p;
    }

    public void setP(Paint p) {
        this.p = p;
    }


    public DrawThread getDrawThread() {
        return drawThread;
    }

    public void setDrawThread(DrawThread drawThread) {
        this.drawThread = drawThread;
    }

}
