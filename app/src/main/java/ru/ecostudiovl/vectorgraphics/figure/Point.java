package ru.ecostudiovl.vectorgraphics.figure;



public class Point {

    public float x, y;
    public int nextIndex;
    public boolean isSelected;

    public Point(float x, float y, int nextIndex){
        this.x = x;
        this.y = y;
        this.isSelected = false;
        this.nextIndex = nextIndex;
    }


}
