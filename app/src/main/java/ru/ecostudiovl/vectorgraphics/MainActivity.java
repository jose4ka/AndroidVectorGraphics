package ru.ecostudiovl.vectorgraphics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;

import ru.ecostudiovl.vectorgraphics.figure.Figure;


public class MainActivity extends AppCompatActivity implements AdapterFiguresList.FigureSelect {


    enum Mode {
        create,
        edit,
        delete,
        view
    }

    public Mode currentMode;

    private EditText etFigureName;
    private Button btnAddFigure;
    private Button btnClear;
    private RecyclerView rvFigures;
    private DrawView drawView;
    private Button btnChangeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvFigures = findViewById(R.id.rvFiguresList);
        etFigureName = findViewById(R.id.etFigureName);

        btnAddFigure = findViewById(R.id.btnAddFigure);
        btnAddFigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSelected();
                drawView.setSelectedFigure(drawView.figures.size());
                drawView.figures.add(new Figure(etFigureName.getText().toString()+" : "+drawView.figures.size()));
                drawView.figures.get(drawView.getSelectedFigure()).isSelected = true;
                etFigureName.setText("");
                updateList();
            }
        });

        FrameLayout frameLayout = findViewById(R.id.frameDraw);
        drawView = new DrawView(this);

        frameLayout.addView(drawView);
        currentMode = Mode.create;
        btnChangeMode = findViewById(R.id.btnChangeMode);
        btnChangeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentMode) {
                    case create:
                        currentMode = Mode.edit;
                        drawView.mode = Mode.edit;
                        btnChangeMode.setText("Edit");
                        break;
                    case edit:
                        currentMode = Mode.delete;
                        drawView.mode = Mode.delete;
                        btnChangeMode.setText("Delete");
                        break;
                    case delete:
                        currentMode = Mode.view;
                        drawView.mode = Mode.view;
                        btnChangeMode.setText("View");
                        clearSelected();
                        drawView.setSelectedFigure(-1);
                        break;
                    case view:
                        currentMode = Mode.create;
                        drawView.mode = Mode.create;
                        btnChangeMode.setText("Create");
                        break;
                }
            }
        });

        btnClear = findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.figures = new ArrayList<>();
                updateList();
            }
        });
        updateList();
    }

    public void updateList(){
        AdapterFiguresList adapterFiguresList = new AdapterFiguresList(drawView.figures, this);
        rvFigures.setAdapter(adapterFiguresList);
        rvFigures.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    @Override
    public void onSelectFigure(int index) {
        drawView.setSelectedFigure(index);

        clearSelected();
        drawView.figures.get(index).isSelected = true;

    }

    private void clearSelected(){
        for (int i = 0; i < drawView.figures.size(); i++){
            drawView.figures.get(i).isSelected = false;
        }
    }
}