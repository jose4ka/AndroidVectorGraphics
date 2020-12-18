package ru.ecostudiovl.vectorgraphics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import ru.ecostudiovl.vectorgraphics.figure.Figure;

public class MainActivity extends AppCompatActivity {


    enum Mode{
        create,
        edit,
        delete
    }

    public Mode currentMode;

    private DrawView drawView;
    private FrameLayout frameLayout;
    private Button btnChangeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("RENDER", "=== POINT 1");
        frameLayout = findViewById(R.id.frameDraw);
        drawView = new DrawView(this);
        Log.d("RENDER", "=== ADD VIEW");
        frameLayout.addView(drawView);
        currentMode = Mode.create;
        btnChangeMode = findViewById(R.id.btnChangeMode);
        btnChangeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentMode){
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
                        currentMode = Mode.create;
                        drawView.mode = Mode.create;
                        btnChangeMode.setText("Create");
                        break;
                }
            }
        });

    }




}