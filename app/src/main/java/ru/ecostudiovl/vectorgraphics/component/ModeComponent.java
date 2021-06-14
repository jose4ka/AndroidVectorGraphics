package ru.ecostudiovl.vectorgraphics.component;

public class ModeComponent {

    //Режим работы программы
    public enum Mode {
        CREATE, //Создание фигуры
        EDIT, //Редактирование фигуры
        DELETE, //Удаление точек
        VIEW, //Просмотр фигур (холста)
        COPY, //Копирование фигуры
        MOVE //Перемещение фигуры
    }

    //Режим выбора фигуры
    public enum SelectionMode{
        ONE, //Одиночный выбор
        MORE //ножественный выбор
    }

    //Режим создания фигуры
    //Пока не используется
    public enum CreationMode{
        ADDITIVE, //Аддитивный, на основе уже созданных точек
        CREATIVE //Креативный, с созданием новых точек
    }


    public Mode currentMode; //Текущий режим работы программы
    public SelectionMode selectionMode; //Текущий режим выбора фигуры
    public CreationMode creationMode; //Текущий режим создания фигуры (не используется)

    private static ModeComponent instance;

    //Конструктор класса
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
