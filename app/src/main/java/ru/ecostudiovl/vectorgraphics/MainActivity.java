package ru.ecostudiovl.vectorgraphics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import ru.ecostudiovl.vectorgraphics.figure.Figure;

public class MainActivity extends AppCompatActivity implements AdapterFiguresList.FigureSelect {

    public enum State{
        Overview,
        FigureCreating,
        FigurePointEdit,
        FigureAddPoints,
        FigureDeletePoints,
        FigureSelected
    }

    private State currentState;

    private Button btnOverView, btnAddF, btnClearF;
    private DrawView drawView;
    private FrameLayout frameLayout;
    private EditText etFigureName;
    private Spinner operationsSpinner;

    private RecyclerView recyclerView;
    private AdapterFiguresList adapterFiguresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        frameLayout = findViewById(R.id.frameDraw);
        drawView = new DrawView(this);
        frameLayout.addView(drawView);

        operationsSpinner = findViewById(R.id.operationsSpinner);
        operationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        switchState(State.FigurePointEdit);
                        break;
                    case 1:
                        switchState(State.FigureAddPoints);
                        break;
                    case 2:
                        switchState(State.FigureDeletePoints);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etFigureName = findViewById(R.id.etFigureName);

        recyclerView = findViewById(R.id.rvFigures);

        btnOverView = findViewById(R.id.btnOverview);
        btnOverView.setEnabled(false);
        btnOverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState(State.Overview);
            }
        });

        btnAddF = findViewById(R.id.btnAddFigure);
        btnAddF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState(State.FigureCreating);

                if (!etFigureName.getText().toString().isEmpty()){
                    switchState(State.FigureCreating);
                }
            }
        });

        btnClearF = findViewById(R.id.btnClearFigures);
        btnClearF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clear();
                btnOverView.setEnabled(false);
                initializeRV();
            }
        });

        etFigureName.clearFocus();

        startPosition();
    }

    private void initializeRV(){
        adapterFiguresList = new AdapterFiguresList(drawView.getFigures(), this);
        recyclerView.setAdapter(adapterFiguresList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public void onSelectFigure(Figure figure) {
        drawView.setSelectedFigure(figure.getFiguresListIndex());
        switchState(State.FigurePointEdit);
    }

    private void startPosition(){
        btnOverView.setVisibility(View.GONE);
        operationsSpinner.setVisibility(View.GONE);
        btnClearF.setVisibility(View.VISIBLE);
    }

    private void switchState(State state){
        currentState = state;
        switch (state){
            case Overview:
                operationsSpinner.setVisibility(View.GONE);
                btnOverView.setEnabled(false);
                drawView.setCurrentState(DrawView.State.Overview);
                break;
            case FigureCreating:
                operationsSpinner.setVisibility(View.GONE);
                btnOverView.setVisibility(View.VISIBLE);
                btnOverView.setEnabled(true);
                etFigureName.clearFocus();
                drawView.addFigure(etFigureName.getText().toString());
                drawView.setCurrentState(DrawView.State.FigureCreating);
                initializeRV();
                etFigureName.setText("");
                break;
            case FigureAddPoints:
                operationsSpinner.setVisibility(View.VISIBLE);
                btnOverView.setEnabled(true);
                btnOverView.setVisibility(View.VISIBLE);
                drawView.setCurrentState(DrawView.State.FigureAddPoints);
                break;
            case FigurePointEdit:
                operationsSpinner.setVisibility(View.VISIBLE);
                btnOverView.setEnabled(true);
                btnOverView.setVisibility(View.VISIBLE);
                drawView.setCurrentState(DrawView.State.FigurePointEdit);
                break;
            case FigureDeletePoints:
                operationsSpinner.setVisibility(View.VISIBLE);
                btnOverView.setEnabled(true);
                btnOverView.setVisibility(View.VISIBLE);
                drawView.setCurrentState(DrawView.State.FigureDeletePoints);
                break;
            case FigureSelected:
                operationsSpinner.setVisibility(View.VISIBLE);
                btnOverView.setVisibility(View.VISIBLE);
                btnOverView.setEnabled(true);
                drawView.setCurrentState(DrawView.State.FigureSelected);
                break;
        }
    }
}