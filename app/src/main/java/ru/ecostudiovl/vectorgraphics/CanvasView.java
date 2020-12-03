package ru.ecostudiovl.vectorgraphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.point.Figure;

public class CanvasView extends View {

    private List<Figure> figures;
    public CanvasView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        figures = new ArrayList<>();
        figures.add(new Figure(0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(255, 255, 255));
        Paint p = new Paint();
        for (int i = 0; i < figures.get(0).getPoints().size(); i++){
            if (i == 0){

                p.setColor(Color.BLACK);
                canvas.drawCircle(figures.get(0).getPoints().get(i).getX(), figures.get(0).getPoints().get(i).getY(), 10, p);
            }
            else if ((i + 1) < figures.get(0).getPoints().size()){
                canvas.drawCircle(figures.get(0).getPoints().get(i).getX(), figures.get(0).getPoints().get(i).getY(), 10, p);

                canvas.drawLine(
                        figures.get(0).getPoints().get(i).getX(),
                        figures.get(0).getPoints().get(i).getY(),
                        figures.get(0).getPoints().get(i + 1).getX(),
                        figures.get(0).getPoints().get(i + 1).getY(),
                        p
                );
            }

            else {
                canvas.drawLine(
                        figures.get(0).getPoints().get(i).getX(),
                        figures.get(0).getPoints().get(i).getY(),
                        figures.get(0).getPoints().get(0).getX(),
                        figures.get(0).getPoints().get(0).getY(),
                        p
                );
            }




        }

        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float column = event.getX();
        float row = event.getY();
        figures.get(0).addPoint(column, row);

        return true;
    }
}
