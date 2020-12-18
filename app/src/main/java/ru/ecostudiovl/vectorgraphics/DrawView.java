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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import ru.ecostudiovl.vectorgraphics.figure.Figure;
import ru.ecostudiovl.vectorgraphics.figure.Point;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public enum State{
        Overview,
        FigureCreating,
        FigurePointEdit,
        FigureAddPoints,
        FigureDeletePoints,
        FigureSelected
    }

    private State currentState;

    private List<Figure> figures;
    private int selectedFigure;

    private int touchedFigure, touchedPoint;

    //Массив 1 - x и y, массив 2 - номер фигуры и точки в ней
    private TreeMap<String, float[]> pointsMap;

    private Paint p = new Paint();

    private DrawThread drawThread;

    private boolean hasSelectedPoint;

    public DrawView(Context context){
        super(context);
        getHolder().addCallback(this);
        initialize();
    }

    private void initialize(){
        figures = new ArrayList<>();
        pointsMap = new TreeMap<>();
        hasSelectedPoint = false;
        selectedFigure = 0;
        touchedFigure = 0;
        touchedPoint = 0;
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
                if (Integer.parseInt(entry.getKey().split(":")[0]) == selectedFigure && !hasSelectedPoint){
                    touchedFigure = Integer.parseInt(entry.getKey().split(":")[0]);
                    touchedPoint = Integer.parseInt(entry.getKey().split(":")[1]);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hasDeleteInteract(float x, float y){

        //Т.к. мы нажимаем на экран, то мы не можем нажать пиксель в пиксель
        int minXDelta = 30;
        int minYDelta = 30;

        for (TreeMap.Entry<String,float[] > entry: pointsMap.entrySet()) {
            if (Math.abs(x - entry.getValue()[0]) <= minXDelta && Math.abs(y - entry.getValue()[1]) <= minYDelta){
                if (Integer.parseInt(entry.getKey().split(":")[0]) == selectedFigure){
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


        switch (currentState){
            case Overview:
                if (hasInteract(column, row)){
                    disableAllPointsSelects();
                    figures.get(touchedFigure).getPoints().get(touchedPoint).setSelected(true);
                    disableAllFiguresSelects();
                    figures.get(touchedFigure).setSelected(true);
                }
                else {
                    disableAllPointsSelects();
                    disableAllFiguresSelects();
                }
                result = true;

                break;
            case FigureCreating:


                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(selectedFigure);
                stringBuilder.append(":");
                stringBuilder.append(figures.get(selectedFigure).getPoints().size());
                Log.d("CREATING", stringBuilder.toString() + "value "+column+":"+row);
                pointsMap.put(stringBuilder.toString(), new float[]{column, row});
                figures.get(selectedFigure).addPoint(column, row);


                result = false;
                break;
            case FigurePointEdit:
                if (event.getAction() == MotionEvent.ACTION_UP){
                    hasSelectedPoint = false;
                    Log.d("EDIT", "DISABLE");
                    disableAllPointsSelects();
                    result = true;
                }
                else if (hasEditInteract(column, row)){
                    if (!hasSelectedPoint){

                        Log.d("EDIT", "ACTIVE POINT = "+selectedFigure+" "+touchedPoint);

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

                result = true;
                break;
            case FigureAddPoints:

                StringBuilder stringBuilderAdd = new StringBuilder();
                stringBuilderAdd.append(selectedFigure);
                stringBuilderAdd.append(":");
                stringBuilderAdd.append(figures.get(selectedFigure).getPoints().size());
                pointsMap.put(stringBuilderAdd.toString(), new float[]{column, row});
                figures.get(selectedFigure).addPoint(column, row);

                result = false;
                break;
            case FigureDeletePoints:
                if (hasDeleteInteract(column, row)){
                    Log.d("DETECTED REMOVE", touchedFigure + " - "+touchedPoint+" in "+column + " : "+row);

                    StringBuilder stringBuilderRemove = new StringBuilder();
                    stringBuilderRemove.append(selectedFigure);
                    stringBuilderRemove.append(":");
                    stringBuilderRemove.append(touchedPoint);

                    pointsMap.remove(stringBuilderRemove.toString());

                    updatePointMapByFigure();
                }
                result = false;
                break;
            case FigureSelected:
                break;
        }


        return result;
    }

    public void updatePointMapByFigure(){

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < figures.get(selectedFigure).getPoints().size(); i++) {
            if (figures.get(selectedFigure).getPoints().get(i).getIndex() != touchedPoint){
                points.add(figures.get(selectedFigure).getPoints().get(i));
            }
        }

        figures.get(selectedFigure).getPoints().clear();

        for (int i = 0; i < points.size(); i++) {
            Log.d("CREATING", points.get(i).getX()+" "+ points.get(i).getY());
            figures.get(selectedFigure).addPoint(points.get(i).getX(), points.get(i).getY());
        }

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
