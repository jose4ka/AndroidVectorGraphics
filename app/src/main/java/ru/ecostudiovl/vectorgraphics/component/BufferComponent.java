package ru.ecostudiovl.vectorgraphics.component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BufferComponent {

    private TreeMap<Integer, Integer> selectedMap;
    private static BufferComponent instance;

    private BufferComponent(){
        selectedMap = new TreeMap<>();
    }

    public static BufferComponent getInstance(){
        if (instance == null){
            instance = new BufferComponent();
        }

        return instance;
    }

    public boolean hasSelectedFigures(){
        return selectedMap.size() > 0;
    }

    public void addFigure(int index){
        selectedMap.put(index, 0);
    }

    public void deleteFigure(int index){
        selectedMap.remove(index);
    }


    public void clearSelected(){
        selectedMap.clear();
    }

    public int getCurrentSelectedObject(){
        if (selectedMap.size()  == 1){
            for (TreeMap.Entry<Integer, Integer> entry:selectedMap.entrySet()) {
                return entry.getKey();
            }

        }
        return  -1;
    }

    public boolean isContainsFigure(int index){
        return selectedMap.containsKey(index);
    }

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
