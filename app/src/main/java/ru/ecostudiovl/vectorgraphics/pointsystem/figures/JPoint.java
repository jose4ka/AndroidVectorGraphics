package ru.ecostudiovl.vectorgraphics.pointsystem.figures;

import org.jetbrains.annotations.NotNull;

public class JPoint {

    private float x; //Позиция точки по  X
    private float y; //Позиция точки по Y

    //Конструктор класса
    public JPoint(float x, float y){
        this.x = x;
        this.y = y;
    }

    @NotNull
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
