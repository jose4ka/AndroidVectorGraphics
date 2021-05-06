package ru.ecostudiovl.vectorgraphics.fragment.constructor;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterTemplatesList;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;

public class FragmentFigureEditor extends Fragment implements AdapterTemplatesList.TemplateSelect {

    private View view;
    private ImageButton btnCreateFigure, btnBack;
    private RecyclerView rvTemplates;
    private EditText etFigureName;

    private int selectedTemplate = -1;

    public interface FragmentFigureEditorCallback{
        void onCreatedFigure();
        void onBackPressedFigure();
    }

    private FragmentFigureEditorCallback fragmentFigureEditorCallback;

    public FragmentFigureEditor() {
        // Required empty public constructor
    }

    public static FragmentFigureEditor newInstance(String param1, String param2) {
        FragmentFigureEditor fragment = new FragmentFigureEditor();
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentFigureEditorCallback = (FragmentFigureEditorCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_figure_editor, container, false);
        initializeViewElements();
        updateList();
        return view;
    }


    public void initializeViewElements(){
        rvTemplates = view.findViewById(R.id.rvTemplates);
        etFigureName = view.findViewById(R.id.tvFigureName);

        btnCreateFigure = view.findViewById(R.id.btnActFigureCreateFigure);
        btnCreateFigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTemplate == -1){
                    Toast.makeText(getContext(), "Вы не выбрали шаблон", Toast.LENGTH_SHORT).show();
                }
                else {
                    createFigure();
                    fragmentFigureEditorCallback.onCreatedFigure();
                }

            }
        });

        btnBack = view.findViewById(R.id.btnActFigureBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentFigureEditorCallback.onBackPressedFigure();
            }
        });


    }

    private void createFigure(){
        JPointData.getInstance().getFigures().add(new JFigure((etFigureName.getText().toString() + " : "+JPointData.getInstance().getFigures().size()), selectedTemplate));
    }

    private void updateList(){
        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, requireContext(), selectedTemplate);
        rvTemplates.setAdapter(adapterTemplatesList);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        rvTemplates.setLayoutManager(layoutManager);
    }


    @Override
    public void onSelectTemplate(int index) {
        selectedTemplate = index;
        updateList();
    }
}