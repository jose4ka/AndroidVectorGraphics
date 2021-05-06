package ru.ecostudiovl.vectorgraphics.component;

public class SettingsComponent {


    private static SettingsComponent instance;

    private boolean showPoints;

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
