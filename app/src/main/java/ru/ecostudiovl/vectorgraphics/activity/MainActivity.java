package ru.ecostudiovl.vectorgraphics.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import ru.ecostudiovl.vectorgraphics.adapter.AdapterFiguresList;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.view.DrawView;
import ru.ecostudiovl.vectorgraphics.R;


public class MainActivity extends AppCompatActivity implements AdapterFiguresList.FigureSelect {


    public enum Mode {
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

    private ImageButton btnLeft, btnRight, btnUp, btnDown;
    private SeekBar seekBarScale;

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

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

                Intent i = new Intent(MainActivity.this, ActivityCreateFigure.class);
                startActivity(i);



                updateList();
            }
        });

        FrameLayout frameLayout = findViewById(R.id.frameDraw);
        drawView = new DrawView(this);

        frameLayout.addView(drawView);
        currentMode = Mode.view;

        btnChangeMode = findViewById(R.id.btnChangeMode);
        btnChangeMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentMode) {
                    case create:
                        currentMode = Mode.edit;
                        drawView.mode = Mode.edit;
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
                        break;
                    case edit:
                        currentMode = Mode.delete;
                        drawView.mode = Mode.delete;
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_delete_24);
                        break;
                    case delete:
                        currentMode = Mode.view;
                        drawView.mode = Mode.view;
                        clearSelected();
                        drawView.setSelectedFigure(-1);
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_preview_24);
                        updateList();
                        break;
                    case view:
                        if (hasSelected()){
                            currentMode = Mode.create;
                            drawView.mode = Mode.create;
                            btnChangeMode.setImageResource(R.drawable.ic_baseline_create_new_folder_24);
                        }

                        break;
                }
            }
        });

        btnClear = findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.getPoints().clear();
                JPointData.getInstance().getFigures().clear();
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
                        break;

                        case View.GONE:
                            lnList.setVisibility(View.VISIBLE);
                            btnHide.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
                            break;
                }
            }
        });

        drawView.setScaleMultiplier(50);
        btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setxDelta(drawView.getxDelta() + 100);
            }
        });

        btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setxDelta(drawView.getxDelta() - 100);
            }
        });

        btnUp = findViewById(R.id.btnUp);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setyDelta(drawView.getyDelta() - 100);
            }
        });

        btnDown = findViewById(R.id.btnDown);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setyDelta(drawView.getyDelta() + 100);
            }
        });

        seekBarScale = findViewById(R.id.seekBar);
        seekBarScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                drawView.setScaleMultiplier(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        updateList();
    }

    public void updateList(){
        AdapterFiguresList adapterFiguresList = new AdapterFiguresList(JPointData.getInstance().getFigures(), this, getApplicationContext());
        rvFigures.setAdapter(adapterFiguresList);
        rvFigures.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    @Override
    public void onSelectFigure(int index) {
        drawView.setSelectedFigure(index);

        clearSelected();
        JPointData.getInstance().getFigures().get(index).setSelected(true);

        if (JPointData.getInstance().getFigures().get(index).getPoints().size() == 0){
            currentMode = Mode.create;
            drawView.mode = Mode.create;
            btnChangeMode.setImageResource(R.drawable.ic_baseline_create_new_folder_24);
        }
        else {
            currentMode = Mode.edit;
            drawView.mode = Mode.edit;
            btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
        }

        
        updateList();
    }

    @Override
    public void onDeletedFigure(int index) {

        drawView.deletePointsWithFigure(index);
        JPointData.getInstance().getFigures().remove(index);

        updateList();
    }



    private void clearSelected(){
        for (int i = 0; i < JPointData.getInstance().getFigures().size(); i++){
            JPointData.getInstance().getFigures().get(i).setSelected(false);
        }
    }

    private boolean hasSelected(){
        for (int i = 0; i < JPointData.getInstance().getFigures().size(); i++){

            if (JPointData.getInstance().getFigures().get(i).isSelected()){
                return true;
            }
        }

        return false;
    }
}