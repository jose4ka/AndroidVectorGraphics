package ru.ecostudiovl.vectorgraphics.fragment.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.ecostudiovl.vectorgraphics.R;

public class FragmentMainMenu extends Fragment {

    private View view; //Корневой элемент фрагмента

    //Объект для колбэка, чтобы к нему обращаться
    private FragmentMainMenuCallback fragmentMainMenuCallback;

    //нтерфейс коллбэка для активности
    public interface FragmentMainMenuCallback{
        void onStartWork();
    }


    public static FragmentMainMenu newInstance() {
        return new FragmentMainMenu();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentMainMenuCallback = (FragmentMainMenuCallback) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        initializeViewElements();
        return view;
    }

    private void initializeViewElements(){
        CardView cvStart = view.findViewById(R.id.cvStartWork);
        cvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentMainMenuCallback.onStartWork();
            }
        });
    }
}