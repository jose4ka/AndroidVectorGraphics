package ru.ecostudiovl.vectorgraphics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import ru.ecostudiovl.vectorgraphics.figure.Figure;

public class MainActivity extends AppCompatActivity implements AdapterFiguresList.FigureSelect {

    private Button btnOverView, btnAddF, btnClearF, btnOperation;
    private DrawView drawView;
    private FrameLayout frameLayout;
    private EditText etFigureName;
    private RecyclerView recyclerView;
    private AdapterFiguresList adapterFiguresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frameDraw);
        drawView = new DrawView(this);
        frameLayout.addView(drawView);

        etFigureName = findViewById(R.id.etFigureName);

        recyclerView = findViewById(R.id.rvFigures);

        btnOverView = findViewById(R.id.btnOverview);
        btnOverView.setEnabled(false);
        btnOverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOperation.setEnabled(false);
                drawView.setCurrentState(DrawView.State.Overview);
                btnOverView.setEnabled(false);
            }
        });

        btnAddF = findViewById(R.id.btnAddFigure);
        btnAddF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFigureName.getText().toString().isEmpty()){
                    btnOverView.setEnabled(true);
                    etFigureName.clearFocus();
                    drawView.addFigure(etFigureName.getText().toString());
                    drawView.setCurrentState(DrawView.State.FigureCreation);
                    initializeRV();
                    etFigureName.setText("");
                }

            }
        });

        btnOperation = findViewById(R.id.btnPointOperation);
        btnOperation.setEnabled(false);
        btnOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOperation.setEnabled(false);
                btnOverView.setEnabled(true);
                drawView.setCurrentState(DrawView.State.PointOperation);
            }
        });

        btnClearF = findViewById(R.id.btnClearFigures);
        btnClearF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clear();
                btnOverView.setEnabled(false);
                btnOperation.setEnabled(false);
                initializeRV();
            }
        });

        etFigureName.clearFocus();
    }

    private void initializeRV(){
        adapterFiguresList = new AdapterFiguresList(drawView.getFigures(), this);
        recyclerView.setAdapter(adapterFiguresList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public void onSelectFigure(Figure figure) {
        btnOperation.setEnabled(true);
        btnOverView.setEnabled(true);
        drawView.setSelectedFigure(figure.getFiguresListIndex());
        drawView.setCurrentState(DrawView.State.Overview);
    }
}