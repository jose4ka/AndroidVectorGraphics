package ru.ecostudiovl.vectorgraphics.pointsystem;

import java.util.ArrayList;
import java.util.List;

import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class JPointData {


    private List<JFigure> figures;
    private List<JFigureTemplates> templates;

    private static JPointData instance;

    private JPointData(){
        figures = new ArrayList<>();
        templates = new ArrayList<>();
        templates.add(new JFigureTemplates(0, false, false, "start template"));
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

    public void setFigures(List<JFigure> figures) {
        this.figures = figures;
    }

    public List<JFigureTemplates> getTemplates() {
        return templates;
    }

    public void setTemplates(List<JFigureTemplates> templates) {
        this.templates = templates;
    }
}
