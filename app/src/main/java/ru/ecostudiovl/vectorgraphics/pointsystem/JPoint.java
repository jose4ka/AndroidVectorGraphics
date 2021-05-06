package ru.ecostudiovl.vectorgraphics.pointsystem;

public class JPoint {

    private float x;
    private float y;
    private int figureIndex;



    public JPoint(float x, float y, int figureIndex){
        this.x = x;
        this.y = y;
        this.figureIndex = figureIndex;
    }




    @Override
    public String toString() {
        return "JPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getFigureIndex() {
        return figureIndex;
    }

    public void setFigureIndex(int figureIndex) {
        this.figureIndex = figureIndex;
    }
}
