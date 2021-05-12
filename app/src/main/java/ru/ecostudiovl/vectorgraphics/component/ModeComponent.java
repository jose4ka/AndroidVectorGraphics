package ru.ecostudiovl.vectorgraphics.component;

public class ModeComponent {

    public enum Mode {
        CREATE,
        EDIT,
        DELETE,
        VIEW
    }

    public enum SelectionMode{
        ONE,
        MORE
    }

    public enum CreationMode{
        ADDITIVE,
        CREATIVE
    }

    public Mode currentMode;
    public SelectionMode selectionMode;
    public CreationMode creationMode;

    private static ModeComponent instance;

    private ModeComponent(){
        currentMode = Mode.VIEW;
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

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }
}
