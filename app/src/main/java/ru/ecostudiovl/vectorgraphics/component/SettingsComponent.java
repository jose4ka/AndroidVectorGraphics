package ru.ecostudiovl.vectorgraphics.component;

public class SettingsComponent {


    private static SettingsComponent instance; //Инстанс класса

    private boolean showPoints; //Отображать точки фигур или нет

    private SettingsComponent(){
        showPoints = true;
    }

    public static SettingsComponent getInstance(){
        if (instance == null){
            instance = new SettingsComponent();
        }

        return instance;
    }

    public boolean isShowPoints() {
        return showPoints;
    }

    public void setShowPoints(boolean showPoints) {
        this.showPoints = showPoints;
    }
}
