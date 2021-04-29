package ru.ecostudiovl.vectorgraphics.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterFiguresList;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterTemplatesList;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;

public class ActivityCreateFigure extends AppCompatActivity implements AdapterTemplatesList.TemplateSelect {

    private RecyclerView rvTemplates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_figure);
        rvTemplates = findViewById(R.id.rvTemplates);
        initializeView();

    }

    private void initializeView(){
        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, getApplicationContext(), 0);
        rvTemplates.setAdapter(adapterTemplatesList);
        rvTemplates.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createFigure(){
//        JPointData.getInstance().getFigures().add(new JFigure((etFigureName.getText().toString() + " : "+JPointData.getInstance().getFigures().size()), 0));
//        drawView.setSelectedFigure(JPointData.getInstance().getFigures().size() - 1);
//        JPointData.getInstance().getFigures().get(drawView.getSelectedFigure()).setSelected(true);
//        etFigureName.setText("");
//
//
//        currentMode = MainActivity.Mode.create;
//        drawView.mode = MainActivity.Mode.create;
//        btnChangeMode.setImageResource(R.drawable.ic_baseline_create_new_folder_24);
    }

    @Override
    public void onSelectTemplate(int index) {

    }

    @Override
    public void onDeletedTemlate(int index) {

    }
}