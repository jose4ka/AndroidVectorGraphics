package ru.ecostudiovl.vectorgraphics.component;

public class ModeComponent {

    public enum Mode {
        create,
        edit,
        delete,
        view
    }

    public Mode currentMode;

    private static ModeComponent instance;

    private ModeComponent(){
        currentMode = Mode.view;
    }

    public static ModeComponent getInstance(){
        if (instance == null){
            instance = new ModeComponent();
        }

        return instance;
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }
}
