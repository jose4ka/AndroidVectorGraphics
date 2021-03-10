package ru.ecostudiovl.vectorgraphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import ru.ecostudiovl.vectorgraphics.figure.Point;

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

        for (int j = 0; j < drawView.figures.size(); j++) {

            for (int i = 0; i < drawView.figures.get(j).points.size(); i++) {
                Point lPoint = drawView.figures.get(j).points.get(i);

                if ((i == 0 || i == drawView.figures.get(j).points.size() - 1) && drawView.figures.get(j).isSelected){
                    p.setColor(Color.GREEN);
                }
                else {
                    p.setColor(Color.BLACK);
                }
                canvas.drawCircle((lPoint.x + drawView.getxDelta()) * drawView.getScaleMultiplier(),
                        (lPoint.y + drawView.getyDelta()) * drawView.getScaleMultiplier(), r, p);


                if (drawView.figures.get(j).isSelected){
                    p.setColor(Color.BLUE);
                }
                else {
                    p.setColor(Color.BLACK);
                }

                canvas.drawLine((lPoint.x + drawView.getxDelta()) * drawView.getScaleMultiplier(),
                        (lPoint.y + drawView.getyDelta()) * drawView.getScaleMultiplier(),
                        (drawView.figures.get(j).points.get(lPoint.nextIndex).x +  + drawView.getxDelta()) * drawView.getScaleMultiplier(),
                        (drawView.figures.get(j).points.get(lPoint.nextIndex).y +  + drawView.getyDelta()) * drawView.getScaleMultiplier(), p);

            }

        }

        canvas.restore();
    }




    public void setRunning(boolean running) {
        this.running = running;
    }

}
