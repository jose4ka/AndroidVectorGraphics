package ru.ecostudiovl.vectorgraphics.fragment.work;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterTemplatesList;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;


public class FragmentTemplateEditor extends Fragment  implements AdapterTemplatesList.TemplateSelect {


    private View view;

    private RecyclerView rvTemplates;
    private Button btnCreateTemplate, btnCreateFigure, btnBack;
    private EditText etTemplateName, etTemplateNumber, etFigureName;
    private CheckBox cbIsClosedFigure, cbIsClosedFigureNumber;

    private int selectedIndex = 0;

    public interface FragmentTemplateEditorCallback{
        void onCreatedFigure();
        void onBackPressedF();
    }

    private FragmentTemplateEditorCallback fragmentTemplateEditorCallback;

    public static FragmentTemplateEditor newInstance(String param1, String param2) {
        FragmentTemplateEditor fragment = new FragmentTemplateEditor();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        fragmentTemplateEditorCallback = (FragmentTemplateEditorCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_template_editor, container, false);
        initializeViewElements();
        initializeRecyclerView();
        return view;
    }

    private void initializeViewElements(){
        rvTemplates = view.findViewById(R.id.rvTemplates);
        etTemplateName = view.findViewById(R.id.tvTemplateName);
        etTemplateNumber = view.findViewById(R.id.tvTemplatePointsCount);
        etFigureName = view.findViewById(R.id.tvFigureName);

        cbIsClosedFigure = view.findViewById(R.id.cbIsClosedNumber);
        cbIsClosedFigureNumber = view.findViewById(R.id.cbIsClosedPontsNumber);

        btnCreateTemplate = view.findViewById(R.id.btnActTempCreateTemplate);
        btnCreateTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbIsClosedFigureNumber.isChecked() && !etTemplateNumber.getText().toString().isEmpty()){
                    int pointsNumber = Integer.parseInt(etTemplateNumber.getText().toString());
                    JPointData.getInstance().getTemplates().add(new JFigureTemplates(
                            pointsNumber,
                            cbIsClosedFigureNumber.isChecked(),
                            cbIsClosedFigure.isChecked(),
                            etTemplateName.getText().toString()

                    ));
                }
                else {
                    JPointData.getInstance().getTemplates().add(new JFigureTemplates(
                            0,
                            cbIsClosedFigureNumber.isChecked(),
                            cbIsClosedFigure.isChecked(),
                            etTemplateName.getText().toString()

                    ));
                }

                initializeRecyclerView();

            }
        });

        btnCreateFigure = view.findViewById(R.id.btnActTempCreateFigure);
        btnCreateFigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFigure();
                fragmentTemplateEditorCallback.onCreatedFigure();
            }
        });

        btnBack = view.findViewById(R.id.btnActTempBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTemplateEditorCallback.onBackPressedF();
            }
        });
    }

    private void initializeRecyclerView(){
        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, requireContext(), 0);
        rvTemplates.setAdapter(adapterTemplatesList);
        rvTemplates.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void createFigure(){
        JPointData.getInstance().getFigures().add(new JFigure((etFigureName.getText().toString() + " : "+JPointData.getInstance().getFigures().size()), selectedIndex));

    }


    @Override
    public void onSelectTemplate(int index) {
        selectedIndex = index;
        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, requireContext(), index);
        rvTemplates.setAdapter(adapterTemplatesList);
        rvTemplates.setLayoutManager(new LinearLayoutManager(requireContext()));
    }



    @Override
    public void onDeletedTemlate(int index) {
        JPointData.getInstance().getTemplates().remove(index);
        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, requireContext(), -1);
        rvTemplates.setAdapter(adapterTemplatesList);
        rvTemplates.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}