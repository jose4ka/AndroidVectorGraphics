package ru.ecostudiovl.vectorgraphics.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.List;

import ru.ecostudiovl.vectorgraphics.component.BufferComponent;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPoint;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;

public class DrawThread extends Thread{

    private boolean running = false;

    private SurfaceHolder surfaceHolder;
    private Paint p;


    public DrawThread(SurfaceHolder surfaceHolder){
        this.surfaceHolder = surfaceHolder;
        this.p = new Paint();
    }


    @Override
    public void run() {

        while (running) {

            double start = System.currentTimeMillis();
            Canvas canvas = null;

            try {
                //15 - время кадра
                Thread.sleep((long) (start + 15 - System.currentTimeMillis()));
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas != null){
                    render(canvas);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void render(Canvas canvas){
        canvas.save();

        canvas.drawColor(Color.rgb(255, 255, 255)); //Рисуем фон
        int r = 3;

        p.setColor(Color.BLACK);


        List<JPoint> points = JPointData.getInstance().getPoints();

        p.setColor(Color.BLACK);

        //Рисуем сами точки
        for (int i = 0; i < points.size(); i++) {
            canvas.drawCircle(points.get(i).getX(), points.get(i).getY(), r, p);
        }

        //Соединяем точки друг с другом
        for (int i = 0; i < JPointData.getInstance().getFigures().size(); i++) {

            List<Integer> lPoints = JPointData.getInstance().getFigures().get(i).getPoints();

            if (BufferComponent.getInstance().getSelectedMap().containsKey(i)){
                p.setColor(Color.BLUE);
            }
            else {
                p.setColor(Color.BLACK);
            }

            for (int j = 0; j < lPoints.size(); j++) {

                if ((j + 1) < lPoints.size()){
                    canvas.drawLine(
                            points.get(lPoints.get(j)).getX(),
                            points.get(lPoints.get(j)).getY(),
                            points.get(lPoints.get(j + 1)).getX(),
                            points.get(lPoints.get(j + 1)).getY(),
                            p);
                }
                else {
                    if (JPointData.getInstance().getTemplates().get(JPointData.getInstance().getFigures().get(i).getTemplateIndex()).isClosedFigure()){
                        canvas.drawLine(
                                points.get(lPoints.get(j)).getX(),
                                points.get(lPoints.get(j)).getY(),
                                points.get(lPoints.get(0)).getX(),
                                points.get(lPoints.get(0)).getY(),
                                p);
                    }

                }


            }

            lPoints = null;
        }



        points = null;
        canvas.restore();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
