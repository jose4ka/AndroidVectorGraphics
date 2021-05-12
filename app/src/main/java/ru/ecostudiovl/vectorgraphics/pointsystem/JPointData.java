package ru.ecostudiovl.vectorgraphics.pointsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class JPointData {


    private List<JFigure> figures;
    private List<JFigureTemplates> templates;

    private List<JPoint> points;/*Список абсолютно всех точек, которые есть на экране и которые задействуются
                                    в структуре данных, описывающих фигуры.*/


    private static JPointData instance;

    private JPointData(){
        figures = new ArrayList<>();
        templates = new ArrayList<>();
        templates.add(new JFigureTemplates(0, false, false, "Кривая"));
        templates.add(new JFigureTemplates(0, false, true, "Замкнутая фигура"));
        templates.add(new JFigureTemplates(3, true, true, "Треугольник"));
        templates.add(new JFigureTemplates(4, true, true, "Четырёхугольник"));
        templates.add(new JFigureTemplates(5, true, true, "Пятиугольник"));
        templates.add(new JFigureTemplates(6, true, true, "Шестиугольник"));
        templates.add(new JFigureTemplates(3, true, false, "Угол"));
        points = new LinkedList<>();
    }

    public static JPointData getInstance(){
        if (instance == null) {
            instance = new JPointData();
        }

        return instance;
    }

    public List<JFigure> getFigures() {
        return figures;
    }


    public List<JFigureTemplates> getTemplates() {
        return templates;
    }


    public List<JPoint> getPoints() {
        return points;
    }


}
