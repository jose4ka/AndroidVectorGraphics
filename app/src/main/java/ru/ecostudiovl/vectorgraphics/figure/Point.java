package ru.ecostudiovl.vectorgraphics.figure;



public class Point {

    public float x, y;
    public int nextIndex;
    public int figure;
    public boolean isSelected;

    public Point(float x, float y, int figure,int nextIndex){
        this.x = x;
        this.y = y;
        this.figure = figure;
        this.isSelected = false;
        this.nextIndex = nextIndex;
    }


}
