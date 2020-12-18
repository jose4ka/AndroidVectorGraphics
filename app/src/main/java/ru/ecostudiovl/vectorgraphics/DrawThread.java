package ru.ecostudiovl.vectorgraphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
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

        p.setColor(Color.BLACK);
        for (int i = 0; i < drawView.points.size(); i++) {
            Point lPoint = drawView.points.get(i);
            canvas.drawCircle(lPoint.x, lPoint.y, r, p);
            canvas.drawLine(lPoint.x, lPoint.y, drawView.points.get(lPoint.nextIndex).x, drawView.points.get(lPoint.nextIndex).y, p);
            canvas.drawText(""+i, lPoint.x, lPoint.y + 20, p);
        }
        canvas.restore();
    }




    public void setRunning(boolean running) {
        this.running = running;
    }

}
