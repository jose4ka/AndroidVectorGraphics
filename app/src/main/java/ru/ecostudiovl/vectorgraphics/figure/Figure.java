package ru.ecostudiovl.vectorgraphics.figure;

import java.util.ArrayList;
import java.util.List;

public class Figure {

    public List<Point> points;
    public String name;
    public boolean isSelected = false;

    public Figure(String name){
        this.points = new ArrayList<>();
        this.name = name;
    }


}
