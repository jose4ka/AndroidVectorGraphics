package ru.ecostudiovl.vectorgraphics.component;

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

    public TreeMap<Integer, Integer> getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(TreeMap<Integer, Integer> selectedMap) {
        this.selectedMap = selectedMap;
    }
}
