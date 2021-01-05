package ru.ecostudiovl.vectorgraphics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
    private ImageButton btnAddFigure;
    private ImageButton btnClear;
    private ImageButton btnHide;
    private LinearLayout lnList;
    private RecyclerView rvFigures;
    private DrawView drawView;
    private ImageButton btnChangeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvFigures = findViewById(R.id.rvFiguresList);
        etFigureName = findViewById(R.id.etFigureName);

        btnAddFigure = findViewById(R.id.btnAddFigure);
        btnAddFigure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSelected();
                drawView.setSelectedFigure(drawView.figures.size());
                drawView.figures.add(new Figure(etFigureName.getText().toString()+" : "+drawView.figures.size()));
                drawView.figures.get(drawView.getSelectedFigure()).isSelected = true;
                etFigureName.setText("");

                currentMode = Mode.create;
                drawView.mode = Mode.create;
//                btnChangeMode.setText("Create");
                updateList();
            }
        });

        FrameLayout frameLayout = findViewById(R.id.frameDraw);
        drawView = new DrawView(this);

        frameLayout.addView(drawView);
        currentMode = Mode.create;
        btnChangeMode = findViewById(R.id.btnChangeMode);
        btnChangeMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentMode) {
                    case create:
                        currentMode = Mode.edit;
                        drawView.mode = Mode.edit;
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
//                        btnChangeMode.setText("Edit");
                        break;
                    case edit:
                        currentMode = Mode.delete;
                        drawView.mode = Mode.delete;
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_delete_24);
//                        btnChangeMode.setText("Delete");
                        break;
                    case delete:
                        currentMode = Mode.view;
                        drawView.mode = Mode.view;
//                        btnChangeMode.setText("View");
                        clearSelected();
                        drawView.setSelectedFigure(-1);
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_preview_24);
                        updateList();
                        break;
                    case view:
                        currentMode = Mode.create;
                        drawView.mode = Mode.create;
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_create_new_folder_24);
//                        btnChangeMode.setText("Create");
                        break;
                }
            }
        });

        btnClear = findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.figures = new ArrayList<>();
                updateList();
            }
        });

        lnList = findViewById(R.id.lnList);

        btnHide = findViewById(R.id.btnShowHide);
        btnHide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (lnList.getVisibility()){
                    case View.VISIBLE:
                        lnList.setVisibility(View.GONE);
                        btnHide.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
//                        btnHide.setText("Show");
                        break;

                        case View.GONE:
                            lnList.setVisibility(View.VISIBLE);
                            btnHide.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
//                            btnHide.setText("Hide");
                            break;
                }
            }
        });
        updateList();
    }

    public void updateList(){
        AdapterFiguresList adapterFiguresList = new AdapterFiguresList(drawView.figures, this, getApplicationContext());
        rvFigures.setAdapter(adapterFiguresList);
        rvFigures.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    @Override
    public void onSelectFigure(int index) {
        drawView.setSelectedFigure(index);

        clearSelected();
        drawView.figures.get(index).isSelected = true;

        currentMode = Mode.edit;
        drawView.mode = Mode.edit;
        btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);

        updateList();
    }

    @Override
    public void onDeletedFigure(int index) {
        drawView.getFigures().remove(index);
        updateList();
    }



    private void clearSelected(){
        for (int i = 0; i < drawView.figures.size(); i++){
            drawView.figures.get(i).isSelected = false;
        }
    }
}