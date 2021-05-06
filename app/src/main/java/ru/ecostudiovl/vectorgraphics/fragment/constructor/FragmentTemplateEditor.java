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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterTemplatesList;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;


public class FragmentTemplateEditor extends Fragment  implements AdapterTemplatesList.TemplateSelect {


    private View view;

    private RecyclerView rvTemplates;
    private ImageButton btnCreateTemplate, btnBack;
    private EditText etTemplateName, etPointsNumber;
    private CheckBox cbIsClosedFigure, cbIsClosedFigureNumber;

    private int selectedIndex = 0;

    private ItemTouchHelper itemTouchHelper;

    public interface FragmentTemplateEditorCallback{
        void onCreatedTemplate();
        void onBackPressedTemplate();
    }

    private FragmentTemplateEditorCallback fragmentTemplateEditorCallback;

    public static FragmentTemplateEditor newInstance(String param1, String param2) {
        FragmentTemplateEditor fragment = new FragmentTemplateEditor();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentTemplateEditorCallback = (FragmentTemplateEditorCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_template_editor, container, false);
        initializeViewElements();
        updateList();
        return view;
    }

    private void initializeViewElements(){
        rvTemplates = view.findViewById(R.id.rvTemplates);
        etTemplateName = view.findViewById(R.id.tvTemplateName);
        etPointsNumber = view.findViewById(R.id.tvTemplatePointsCount);
        etPointsNumber.setEnabled(false);

        cbIsClosedFigure = view.findViewById(R.id.cbIsClosedNumber);
        cbIsClosedFigureNumber = view.findViewById(R.id.cbIsClosedPontsNumber);
        cbIsClosedFigureNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    etPointsNumber.setEnabled(true);
                }
                else {
                    etPointsNumber.setEnabled(false);
                }
            }
        });

        btnCreateTemplate = view.findViewById(R.id.btnActTempCreateTemplate);
        btnCreateTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbIsClosedFigureNumber.isChecked() && !etPointsNumber.getText().toString().isEmpty()){
                    int pointsNumber = Integer.parseInt(etPointsNumber.getText().toString());
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

                updateList();

            }
        });


        btnBack = view.findViewById(R.id.btnActTempBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTemplateEditorCallback.onBackPressedTemplate();
            }
        });
    }


    @Override
    public void onSelectTemplate(int index) {
        selectedIndex = index;
        updateList();

    }



    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            selectedIndex = -1;
            int position = viewHolder.getAdapterPosition();
            JPointData.getInstance().getTemplates().remove(position);
            updateList();
        }
    };


    private void updateList(){
        simpleItemTouchCallback.setDefaultSwipeDirs(ItemTouchHelper.UP | ItemTouchHelper.DOWN);

        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, requireContext(), selectedIndex);
        rvTemplates.setAdapter(adapterTemplatesList);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        rvTemplates.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback itemTouchHelperCallback;
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvTemplates);


    }
}