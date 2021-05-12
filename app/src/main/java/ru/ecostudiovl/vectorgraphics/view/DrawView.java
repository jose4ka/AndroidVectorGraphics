package ru.ecostudiovl.vectorgraphics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.ecostudiovl.vectorgraphics.component.BufferComponent;
import ru.ecostudiovl.vectorgraphics.component.ModeComponent;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPoint;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private int touchedPoint; //Индекс точки, на которую мы тыкнули пальцем.
    private boolean isPointTouched; //Переменная нужна при перемещении точки, чтобы не цеплять точки которые очень рядом.
    private TreeMap<Float, List<JPoint>> pairsMap; /*Мапа, которая используется для поиска пар точек
    Ключ - расстояние между точками
    Значение - список с самими точками*/



    /*
    TODO: реализовать нормальное перемещение по холсту, и масштабирование
    Вспомогательные перменные которые на данный момент не используются.
    Нужны для перемещения по холсту, и для масштабирования изображения.
     */
    private int scaleMultiplier = 1;
    private float xDelta;
    private float yDelta;

    private DrawThread drawThread;//Главный поток, отвечающий за постоянную отрисовку фигур и точек (рендеринг).



    //Интерфейс коллбэка, который нужен для обращения к фраменту
    public interface DrawViewCallback{
        void onSelectFigure(int index);
    }

    private DrawViewCallback drawViewCallback;

    public DrawView(Context context, DrawViewCallback drawViewCallback) {
        super(context);
        this.drawViewCallback = drawViewCallback;
        getHolder().addCallback(this);
        initializeVariables();
    }

    private void initializeVariables(){
        touchedPoint = -1; //Изначально -1, т.к. точек никаких нет
        isPointTouched = false;
        this.pairsMap = new TreeMap<>();
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
        if (!JPointData.getInstance().getFigures().isEmpty()){
            switch (ModeComponent.getInstance().getCurrentMode()) {
                case CREATE:
                    if (ModeComponent.getInstance().getSelectionMode() == ModeComponent.SelectionMode.ONE) {/*
                            Режим создания точек
                            Срабатывает если у нас выбрана хоть какая-то фигура
                            Далее, мы получаем шаблон у выбранной фигуры, и смотрим ограничения по точкам у этого шаблона
                            Если есть ограничения - добавляем точки с учетом ограничений
                            Если ограничений нет - просто добавляем точки*/
                        if (BufferComponent.getInstance().hasSelectedFigures()) {

                            int template = JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).getTemplateIndex();
                            int pointsCount = JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).getPoints().size();
                            if (JPointData.getInstance().getTemplates().get(template).isClosePointNumber()) {
                                if (pointsCount < JPointData.getInstance().getTemplates().get(template).getPointsCount()) {
                                    JPointData.getInstance().getPoints().add(new JPoint(x, y)); //Добавляем точку в общий список с точками
                                    JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).addPoint(JPointData.getInstance().getPoints().size() - 1, JPointData.getInstance().getTemplates().get(JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).getTemplateIndex()));
                                }
                            } else {
                                JPointData.getInstance().getPoints().add(new JPoint(x, y)); //Добавляем точку в общий список с точками
                                JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).addPoint(JPointData.getInstance().getPoints().size() - 1, JPointData.getInstance().getTemplates().get(JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).getTemplateIndex())); /*Добавляем
                            индекс точки в структуру данных*/
                            }


                        }
                    }


                    return false;
                case EDIT:/*
                Режим редактирования точек
                Срабатывает только если мы выбрали какую-то фигуру*/
                    if (ModeComponent.getInstance().getSelectionMode() == ModeComponent.SelectionMode.ONE) {
                        if (BufferComponent.getInstance().hasSelectedFigures()) {


                            if (!isPointTouched) {
                                touchedPoint = getTouchedPoint(x, y);
                            }

                            if (touchedPoint != -1) {
                                isPointTouched = true;
                            }

                            //Если мы подняли палец, значит никакая точка не выбрана
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                isPointTouched = false;
                                touchedPoint = -1;
                            }

                            if (JPointData.getInstance().getPoints().size() > 0) {
                                if (JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).isContainsPoint(touchedPoint)) {
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
                    }




                    return true;

                case DELETE:
                    /*
                    Режим удаления точек
                    Срабатывает только если мы выбрали какую-то фигуру*/

                    if (ModeComponent.getInstance().getSelectionMode() == ModeComponent.SelectionMode.ONE) {
                        if (BufferComponent.getInstance().hasSelectedFigures()) {
                            touchedPoint = getTouchedPoint(x, y);
                            if (touchedPoint != -1) {
                                JPointData.getInstance().getPoints().remove(touchedPoint); //Удаляем точку из общего списка
                                JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).deletePoint(touchedPoint); //Удаляем точку из текущей фигуры
                                minimize(touchedPoint); //Минимизируем все точки в структуре данных
                            }

                        }
                    }



                    return false;

                case VIEW:/*
                Режим простого просмотра фигур
                Тут мы можем выбрать фигуру с помощью нажатия на точку или линию
                */
                    touchedPoint = getTouchedPoint(x, y);
                    if (touchedPoint == -1){
                        findFigureByLine(x, y);
                    }
                    else {
                        selectFigureByPoint(findFigureByPoint(touchedPoint));
                    }

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
                            Math.pow((JPointData.getInstance().getPoints().get(i).getY() - centerY),2)) <
                    (Math.pow(radius, 2));

            if (pointInRadius) {
                result = i;
            }
        }
        return result;
    }

    /*
    Процедура находит фигуру, на которую мы нажали
    На вход передаётся координаты x и y, где было произведено нажатие
    1. Находим все пары точек, в зоне которых находятся координаты
    2. Находим пару точек, расстояние между которыми минимальное
    3. Если такая пара найдена, находим угол поворота между первой точкой, и координатами
    4. Далее, находим угол поворота между точками в паре
    5. Если угол координат, совпадает с углом второй точки (с учётом погрешности),
     то мы нашли нужное нам пересечение с линией
    6. Находим фигуру, благодаря процедуре которая находит фигуру по точке (используем первую точку в паре)
     */
    private void findFigureByLine(float x, float y){
        findCollisedPairs(x, y);

        List<JPoint> minimalPair = findMinimalPair();

        if (minimalPair != null){
            JPoint sourcePoint = minimalPair.get(0);
            JPoint destPoint = minimalPair.get(1);

            float currPDegrees = getDegrees(sourcePoint.getX(), sourcePoint.getY(), x, y);

            float nextPDegrees = getDegrees(sourcePoint.getX(), sourcePoint.getY(), destPoint.getX(), destPoint.getY());

            if ((currPDegrees <= nextPDegrees + 20) && (currPDegrees >= nextPDegrees - 20)){
                int index = findFigureByPoint(getTouchedPoint(sourcePoint.getX(), sourcePoint.getY()));
                if (index != -1){
                    drawViewCallback.onSelectFigure(index);
                }

            }
        }
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

    /*
    Процедура удаляет фигуру, а так-же все точки которые ей принадлежат
     */
    public void deletePointsWithFigure(int figureIndex){
        for (int i = 0; i < JPointData.getInstance().getFigures().get(figureIndex).getPoints().size() ; i++) {
            JPointData.getInstance().getPoints().remove(JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(i).intValue());
            minimize(JPointData.getInstance().getFigures().get(figureIndex).getPoints().get(i));
        }
    }

    //Процедура находит блину отрезка между двумя точками
    private float getLength(float startX, float startY, float endX, float endY){
        return (float) Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2));
    }

    //Процедура находит угол поворота одной точки, относительно другой
    private float getDegrees(float aX, float aY, float bX, float bY){
        double angle = Math.toDegrees(Math.atan2(bX - aX, bY - aY));
        angle = angle + Math.ceil( -angle / 360 ) * 360;
        return (float) angle;
    }


    /*
    Процедура находит самую ближайшую точку к переданным координатам
     */
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
            return index;
        }
        else {
            return  -1;
        }

    }


    /*
    Процедура находит все пары точек, в зоне области которых находятся переданные координаты
     */
    private void findCollisedPairs(float x, float y){
        pairsMap.clear();
        for (int i = 0; i < JPointData.getInstance().getFigures().size(); i++) {
            JFigure figure = JPointData.getInstance().getFigures().get(i);
            JFigureTemplates template = JPointData.getInstance().getTemplates().get(figure.getTemplateIndex());
            for (int j = 0; j < figure.getPoints().size(); j++) {
                JPoint startPoint = new JPoint(0, 0);
                JPoint endPoint = new JPoint(0, 0);

                if ((j + 1) < figure.getPoints().size()){
                    startPoint = JPointData.getInstance().getPoints().get(figure.getPoints().get(j));
                    endPoint = JPointData.getInstance().getPoints().get(figure.getPoints().get(j+1));
                }


                if (template.isClosedFigure() && j == figure.getPoints().size() - 1) {
                    startPoint = JPointData.getInstance().getPoints().get(figure.getPoints().get(j));
                    endPoint = JPointData.getInstance().getPoints().get(figure.getPoints().get(0));
                }

                if (isPointInZone(x, y, startPoint, endPoint)){
                    List<JPoint> lList = new ArrayList<>();
                    lList.add(startPoint);
                    lList.add(endPoint);

                    Float oLength = getLength(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                    pairsMap.put(oLength, lList);
                }

            }
        }
    }

    /*
    Процедура находит пару точек, расстояние между которыми минимальное
     */
    private List<JPoint> findMinimalPair(){

        Map.Entry<Float, List<JPoint>> minEntry = null;
        for (Map.Entry<Float, List<JPoint>> entry : pairsMap.entrySet()) {
            if (minEntry == null || entry.getKey() < minEntry.getKey()) {
                minEntry = entry;
            }
        }
        if (minEntry == null){
            return new LinkedList<>();
        }

        return minEntry.getValue();
    }

    /*
    Процедура проверяет нахождение точки в прямоугольной области
    Учитывается то, в каком направлении идёт фигура
    Вверх, вниз, влево, вправо
     */
    private boolean isPointInZone(float x, float y, JPoint startPoint, JPoint endPoint){

        boolean result = false;
        if (startPoint.getX() <= endPoint.getX()){

            if (startPoint.getY() <= endPoint.getY()){
                if (startPoint.getX() <= x && x <= endPoint.getX() &&
                        startPoint.getY() <= y && y <= endPoint.getY()){
                    result = true;
                }
            }
            else {
                if(startPoint.getX() <= x && x <= endPoint.getX() &&
                        startPoint.getY() >= y && y >= endPoint.getY()){
                    result = true;
                }
            }

        }
        else {
            if (startPoint.getY() <= endPoint.getY()){
                if (startPoint.getX() >= x && x >= endPoint.getX() &&
                        startPoint.getY() <= y && y <= endPoint.getY()){
                    result = true;
                }
            }
            else {
                if(startPoint.getX() >= x && x >= endPoint.getX() &&
                        startPoint.getY() >= y && y >= endPoint.getY()){
                    result = true;
                }
            }
        }

        return result;
    }

    /*
    Процедура находит фигуру, которой принадлежит индекс переданной точки
     */
    private int findFigureByPoint(int point){
        for (int i = 0; i < JPointData.getInstance().getFigures().size(); i++) {
            if (JPointData.getInstance().getFigures().get(i).isContainsPoint(point)){
                return i;
            }

        }
        return -1;
    }

    private void selectFigureByPoint(int figureIndex){
        drawViewCallback.onSelectFigure(figureIndex);
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
