package ru.ecostudiovl.vectorgraphics.pointsystem;

public class JPoint {

    private float x;
    private float y;



    public JPoint(float x, float y){
        this.x = x;
        this.y = y;
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
