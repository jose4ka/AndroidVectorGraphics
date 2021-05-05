package ru.ecostudiovl.vectorgraphics.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import ru.ecostudiovl.vectorgraphics.fragment.work.FragmentDrawer;
import ru.ecostudiovl.vectorgraphics.fragment.work.FragmentTemplateEditor;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.view.DrawView;
import ru.ecostudiovl.vectorgraphics.R;


public class MainActivity extends AppCompatActivity implements FragmentTemplateEditor.FragmentTemplateEditorCallback, FragmentDrawer.FragmentDrawerCallback {


    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);

    }


    @Override
    public void onCreateFigureClicked() {
        navController.navigate(R.id.fragmentTemplateEditor);
    }

    @Override
    public void onCreatedFigure() {
        navController.navigate(R.id.fragmentDrawer);
    }

    @Override
    public void onBackPressedF() {
        navController.navigate(R.id.fragmentDrawer);
    }
}