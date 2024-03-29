package ru.ecostudiovl.vectorgraphics.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;

import ru.ecostudiovl.vectorgraphics.fragment.constructor.FragmentFigureEditor;
import ru.ecostudiovl.vectorgraphics.fragment.work.FragmentDrawer;
import ru.ecostudiovl.vectorgraphics.fragment.constructor.FragmentTemplateEditor;
import ru.ecostudiovl.vectorgraphics.R;


public class MainActivity extends AppCompatActivity implements FragmentTemplateEditor.FragmentTemplateEditorCallback, FragmentDrawer.FragmentDrawerCallback, FragmentFigureEditor.FragmentFigureEditorCallback {


    private NavController navController; //Навигационный контроллер, для переключения экранов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);

    }


    @Override
    public void onCreateFigureClicked() {
        navController.navigate(R.id.fragmentFigureEditor);
    }

    @Override
    public void onCreateTemplateClicked() {
        navController.navigate(R.id.fragmentTemplateEditor);
    }

    @Override
    public void onBackPressedTemplate() {
        navController.navigate(R.id.fragmentDrawer);
    }

    @Override

    public void onCreatedFigure() {
        navController.navigate(R.id.fragmentDrawer);
    }

    @Override
    public void onBackPressedFigure() {
        navController.navigate(R.id.fragmentDrawer);
    }

    @Override
    public void onMainMenuClicked() {
        Intent i = new Intent(MainActivity.this, ActivityMainMenu.class);
        startActivity(i);
        finish();
    }
}