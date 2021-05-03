package ru.ecostudiovl.vectorgraphics.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterFiguresList;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterTemplatesList;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class ActivityCreateFigure extends AppCompatActivity implements AdapterTemplatesList.TemplateSelect {

    private RecyclerView rvTemplates;
    private Button btnCreateTemplate, btnCreateFigure, btnBack;
    private EditText etTemplateName, etTemplateNumber, etFigureName;
    private CheckBox cbIsClosedFigure, cbIsClosedFigureNumber;

    private int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_figure);

        initializeView();
        initializeRecyclerView();

    }

    private void initializeView(){
        rvTemplates = findViewById(R.id.rvTemplates);
        etTemplateName = findViewById(R.id.tvTemplateName);
        etTemplateNumber = findViewById(R.id.tvTemplatePointsCount);
        etFigureName = findViewById(R.id.tvFigureName);

        cbIsClosedFigure = findViewById(R.id.cbIsClosedNumber);
        cbIsClosedFigureNumber = findViewById(R.id.cbIsClosedPontsNumber);

        btnCreateTemplate = findViewById(R.id.btnActTempCreateTemplate);
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

        btnCreateFigure = findViewById(R.id.btnActTempCreateFigure);
        btnCreateFigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFigure();
                finish();
            }
        });

        btnBack = findViewById(R.id.btnActTempBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeRecyclerView(){
        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, getApplicationContext(), 0);
        rvTemplates.setAdapter(adapterTemplatesList);
        rvTemplates.setLayoutManager(new LinearLayoutManager(this));
    }




    private void createFigure(){
        JPointData.getInstance().getFigures().add(new JFigure((etFigureName.getText().toString() + " : "+JPointData.getInstance().getFigures().size()), selectedIndex));

    }

    @Override
    public void onSelectTemplate(int index) {
        selectedIndex = index;
        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, getApplicationContext(), index);
        rvTemplates.setAdapter(adapterTemplatesList);
        rvTemplates.setLayoutManager(new LinearLayoutManager(this));
    }



    @Override
    public void onDeletedTemlate(int index) {
        JPointData.getInstance().getTemplates().remove(index);
        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, getApplicationContext(), -1);
        rvTemplates.setAdapter(adapterTemplatesList);
        rvTemplates.setLayoutManager(new LinearLayoutManager(this));
    }
}