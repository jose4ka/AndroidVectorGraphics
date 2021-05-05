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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMainMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMainMenu extends Fragment {

    private View view;
    private CardView cvStart;

    private FragmentMainMenuCallback fragmentMainMenuCallback;

    public interface FragmentMainMenuCallback{
        void onStartWork();
    }


    public static FragmentMainMenu newInstance(String param1, String param2) {
        FragmentMainMenu fragment = new FragmentMainMenu();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentMainMenuCallback = (FragmentMainMenuCallback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        initializeViewElements();
        return view;
    }

    private void initializeViewElements(){
        cvStart = view.findViewById(R.id.cvStartWork);
        cvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentMainMenuCallback.onStartWork();
            }
        });
    }
}