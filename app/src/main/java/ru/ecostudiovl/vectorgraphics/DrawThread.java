package ru.ecostudiovl.vectorgraphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import ru.ecostudiovl.vectorgraphics.figure.Point;

public class DrawThread extends Thread{

    private boolean running = false;

    private SurfaceHolder surfaceHolder;
    private DrawView drawView;
    private Paint p;
    private Canvas canvas;
    private Rect clipBounds;


    public DrawThread(SurfaceHolder surfaceHolder, DrawView drawView){
        this.surfaceHolder = surfaceHolder;
        this.drawView = drawView;
        this.p = new Paint();
    }


    @Override
    public void run() {

        while (running) {

            double start = System.currentTimeMillis();
            canvas = null;

            try {
                //15 - время кадра
                Thread.sleep((long) (start + 15 - System.currentTimeMillis()));
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas == null) {continue;}
                else {
                    render(canvas);
                }

            }
            catch (Exception e) {}

            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void render(Canvas canvas){
        clipBounds = canvas.getClipBounds();
        canvas.save();

        canvas.drawColor(Color.rgb(255, 255, 255));
        int r = 5;

        if (drawView.getFigures().size() > 0){
            for (int j = 0; j < drawView.getFigures().size(); j++) {
                for (int i = 0; i < drawView.getFigures().get(j).getPoints().size(); i++){
                    Point currentPoint = drawView.getFigures().get(j).getPoints().get(i);

                    if (i == 0){
                        checkSelectedPoint(j, i);
                        canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), r, p);
                    }

                    if(i == drawView.getFigures().get(j).getPoints().size() - 1){
                        Point firstPoint = drawView.getFigures().get(j).getPoints().get(0);

                        checkSelectedFigure(j);
                        canvas.drawLine(firstPoint.getX(), firstPoint.getY(), currentPoint.getX(), currentPoint.getY(), p);

                        checkSelectedPoint(j, i);
                        canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), r, p);
                    }
                    else {
                        Point nextPoint = drawView.getFigures().get(j).getPoints().get(i + 1);

                        checkSelectedFigure(j);
                        canvas.drawLine(currentPoint.getX(), currentPoint.getY(), nextPoint.getX(), nextPoint.getY(), p);

                        checkSelectedPoint(j, i);
                        canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), r, p);
                    }


                }
            }

        }
        canvas.restore();
    }

    private void checkSelectedPoint(int figure, int point){


        if (drawView.getFigures().get(figure).getPoints().get(point).isSelected()){
            p.setColor(Color.RED);
        }

        else if (drawView.getFigures().get(figure).isSelected() && point == 0){
            p.setColor(Color.GREEN);
        }
        else if (drawView.getFigures().get(figure).isSelected() && point == drawView.getFigures().get(figure).getPoints().size() - 1){
            p.setColor(Color.GREEN);
        }

        else {
            p.setColor(Color.BLACK);
        }
    }

    private void checkSelectedFigure(int figure){
        if (drawView.getFigures().get(figure).isSelected()){
            p.setColor(Color.BLUE);
        }
        else {
            p.setColor(Color.BLACK);
        }
    }

    public Rect getClipBounds(){
        return clipBounds;
    }
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
