package ru.ecostudiovl.vectorgraphics.pointsystem;

public class JPoint {

    private float x;
    private float y;
    private int index;


    public JPoint(float x, float y, int index){
        this.x = x;
        this.y = y;
        this.index = index;
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

}
