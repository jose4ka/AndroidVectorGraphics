package ru.ecostudiovl.vectorgraphics.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.fragment.menu.FragmentMainMenu;

public class ActivityMainMenu extends AppCompatActivity implements FragmentMainMenu.FragmentMainMenuCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    public void onStartWork() {
        Intent i = new Intent(ActivityMainMenu.this, MainActivity.class);
        startActivity(i);
    }
}