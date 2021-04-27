package ru.ecostudiovl.vectorgraphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.JPair;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPoint;

public class DrawThread extends Thread{

    private boolean running = false;

    private SurfaceHolder surfaceHolder;
    private DrawView drawView;
    private Paint p;


    public DrawThread(SurfaceHolder surfaceHolder, DrawView drawView){
        this.surfaceHolder = surfaceHolder;
        this.drawView = drawView;
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

        canvas.drawColor(Color.rgb(255, 255, 255));
        int r = 5;

        p.setColor(Color.BLACK);


        List<JPoint> points = drawView.getPoints();
        for (int i = 0; i < points.size(); i++) {
            p.setColor(Color.BLACK);
            canvas.drawCircle(points.get(i).getX(), points.get(i).getY(), 5, p);
            p.setColor(Color.RED);
            canvas.drawText(i+"", points.get(i).getX(), points.get(i).getY() + 20, p);
            canvas.drawText("X: "+points.get(i).getX(), points.get(i).getX(), points.get(i).getY() + 40, p);
            canvas.drawText("Y: "+points.get(i).getY(), points.get(i).getX(), points.get(i).getY() + 60, p);
        }


        p.setColor(Color.BLACK);

        for (int i = 0; i < drawView.getjPointData().getFigures().size(); i++) {


            List<Integer> lPoints = drawView.getjPointData().getFigures().get(i).getPoints();

            for (int j = 0; j < lPoints.size() - 1; j++) {
                p.setColor(Color.BLACK);


                canvas.drawLine(
                        points.get(lPoints.get(j)).getX(),
                        points.get(lPoints.get(j)).getY(),
                        points.get(lPoints.get(j) + 1).getX(),
                        points.get(lPoints.get(j) + 1).getY(),
                        p);


                //Если фигуры закрытая, то ещё рисуем замыкающую линию
                if (drawView.getjPointData().getFigures().get(i).isClosedFigure()){
                    canvas.drawLine(
                            points.get(lPoints.get(j)).getX(),
                            points.get(lPoints.get(j)).getY(),
                            points.get(0).getX(),
                            points.get(0).getY(),
                            p);
                }


                p.setColor(Color.BLUE);

            }
        }



        points = null;
        canvas.restore();
    }




    public void setRunning(boolean running) {
        this.running = running;
    }

}
