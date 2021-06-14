package ru.ecostudiovl.vectorgraphics.component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BufferComponent {

    private TreeMap<Integer, Integer> selectedMap;//Мапа в которой хранятся все выбранные фигуры
    private static BufferComponent instance; //Объект этого класса, нуэжен для реализации паттерна Singleton

    //Конструктор класса
    private BufferComponent(){
        selectedMap = new TreeMap<>();
    }

    public static BufferComponent getInstance(){
        if (instance == null){
            instance = new BufferComponent();
        }
        return instance;
    }

    /*
    Метод возвращает булевое значение, которое даёт знать
    есть ли в этом компоненте выбранные фигуры
     */
    public boolean hasSelectedFigures(){
        return selectedMap.size() > 0;
    }

    /*
    Процедура добавляет индекс фигуры с мапу
    На вход подаётся индекс фигуры
     */
    public void addFigure(int index){
        selectedMap.put(index, 0);
    }

    /*
    Процедура удаляет индекс фигуры из мапы
     */
    public void deleteFigure(int index){
        selectedMap.remove(index);
    }

    /*
    Процедура очищает мапу выбранных фигур
     */
    public void clearSelected(){
        selectedMap.clear();
    }

    /*
    Метод возвращает одну единственную выбранную фигуру в мапе
    Возвращается именно индекс
     */
    public int getCurrentSelectedObject(){
        if (selectedMap.size()  == 1){
            for (TreeMap.Entry<Integer, Integer> entry:selectedMap.entrySet()) {
                return entry.getKey();
            }

        }
        return  -1;
    }

    /*
    Процедура проверяет, содержит ли мапа индекс фигуры
     */
    public boolean isContainsFigure(int index){
        return selectedMap.containsKey(index);
    }

    /*
    Процедура минимизирует индексы всех выбранных фигур
    относительно переданной переменной

    Если текущий индекс фигуры больше чем переданная переменная - понижаем её индекс на 1
     */
    public void minimize(int index){

        List<Integer> lList = new ArrayList<>();
        for (TreeMap.Entry<Integer, Integer> entry:selectedMap.entrySet()) {
            if (entry.getKey() > index){
                lList.add(entry.getKey() - 1);
            }
        }

        selectedMap.clear();

        for (int i = 0; i < lList.size(); i++) {
            selectedMap.put(lList.get(i), 0);
        }
        lList.clear();


    }



    public Map<Integer, Integer> getSelectedMap() {
        return selectedMap;
    }


}
