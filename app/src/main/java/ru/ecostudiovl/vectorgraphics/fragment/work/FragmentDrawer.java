package ru.ecostudiovl.vectorgraphics.fragment.work;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterFiguresList;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.view.DrawView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDrawer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDrawer extends Fragment  implements AdapterFiguresList.FigureSelect, DrawView.DrawViewCallback {


    private View view;

    public enum Mode {
        create,
        edit,
        delete,
        view
    }

    public Mode currentMode;


    private ImageButton btnAddFigure;
    private ImageButton btnClear;
    private ImageButton btnHide;
    private ImageButton btnOverview;
    private ImageButton btnOpenTemplater;
    private ImageButton btnOpenMenu;
    private CardView lnList;
    private RecyclerView rvFigures;
    private DrawView drawView;
    private ImageButton btnChangeMode;
    private ItemTouchHelper itemTouchHelper;
    private TextView tvInfoLabel;

    private ImageButton btnLeft, btnRight, btnUp, btnDown;
    private SeekBar seekBarScale;

    public interface FragmentDrawerCallback{
        void onCreateFigureClicked();
        void onCreateTemplateClicked();
        void onMainMenuClicked();
    }
    private FragmentDrawerCallback fragmentDrawerCallback;

    public FragmentDrawer() {
        // Required empty public constructor
    }


    public static FragmentDrawer newInstance(String param1, String param2) {
        FragmentDrawer fragment = new FragmentDrawer();
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentDrawerCallback = (FragmentDrawerCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_drawer, container, false);
        initializeViewElements();
        updateList();
        return view;
    }

    private void initializeViewElements(){
        rvFigures = view.findViewById(R.id.rvFiguresList);
        tvInfoLabel = view.findViewById(R.id.tvSelectedMode);
        btnAddFigure = view.findViewById(R.id.btnAddFigure);
        btnAddFigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerCallback.onCreateFigureClicked();
            }
        });

        FrameLayout frameLayout = view.findViewById(R.id.frameDraw);
        drawView = new DrawView(getContext(), this);

        frameLayout.addView(drawView);
        currentMode = Mode.view;

        btnChangeMode = view.findViewById(R.id.btnChangeMode);
        btnChangeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSelected()){
                switch (currentMode) {
                    case create:
                        currentMode = Mode.edit;
                        drawView.mode = Mode.edit;
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
                        tvInfoLabel.setText("Редактирование фигуры");
                        break;
                    case edit:
                        currentMode = Mode.delete;
                        drawView.mode = Mode.delete;
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_delete_24);
                        tvInfoLabel.setText("Удаление точек");
                        break;
                    case delete:
                        currentMode = Mode.create;
                        drawView.mode = Mode.create;
                        btnChangeMode.setImageResource(R.drawable.ic_baseline_add_circle_24);
                        tvInfoLabel.setText("Создание точек");
                        break;
                }
                }
            }
        });

        btnOverview = view.findViewById(R.id.btnOverview);
        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMode = Mode.view;
                drawView.mode = Mode.view;
                clearSelected();
                drawView.setSelectedFigure(-1);
                updateList();
                tvInfoLabel.setText("Обзор");
            }
        });

        btnClear = view.findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JPointData.getInstance().getPoints().clear();
                JPointData.getInstance().getFigures().clear();
                updateList();
                tvInfoLabel.setText("Холст очищен!");
            }
        });

        btnOpenTemplater = view.findViewById(R.id.btnWorkOpenTemplateEditor);
        btnOpenTemplater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentDrawerCallback.onCreateTemplateClicked();
            }
        });

        lnList = view.findViewById(R.id.lnList);

        btnHide = view.findViewById(R.id.btnShowHide);
        btnHide.setOnClickListener(new View.OnClickListener() {
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

        btnOpenMenu = view.findViewById(R.id.btnWorkGoToMenu);
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentDrawerCallback.onMainMenuClicked();
            }
        });

        drawView.setScaleMultiplier(50);
        btnLeft = view.findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setxDelta(drawView.getxDelta() + 100);
            }
        });

        btnRight = view.findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setxDelta(drawView.getxDelta() - 100);
            }
        });

        btnUp = view.findViewById(R.id.btnUp);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setyDelta(drawView.getyDelta() - 100);
            }
        });

        btnDown = view.findViewById(R.id.btnDown);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setyDelta(drawView.getyDelta() + 100);
            }
        });

        seekBarScale = view.findViewById(R.id.seekBar);
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

    }


    public void updateList(){

        simpleItemTouchCallback.setDefaultSwipeDirs(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        AdapterFiguresList adapterFiguresList = new AdapterFiguresList(JPointData.getInstance().getFigures(), this, requireContext());
        rvFigures.setAdapter(adapterFiguresList);
        rvFigures.setLayoutManager(new LinearLayoutManager(requireContext()));

        ItemTouchHelper.Callback itemTouchHelperCallback;
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvFigures);
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


    @Override
    public void onSelectFigure(int index) {
        drawView.setSelectedFigure(index);

        clearSelected();
        JPointData.getInstance().getFigures().get(index).setSelected(true);

        if (JPointData.getInstance().getFigures().get(index).getPoints().size() == 0){
            currentMode = Mode.create;
            drawView.mode = Mode.create;
            btnChangeMode.setImageResource(R.drawable.ic_baseline_add_circle_24);
            tvInfoLabel.setText("Создание точек");
        }
        else {
            currentMode = Mode.edit;
            drawView.mode = Mode.edit;
            btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
            tvInfoLabel.setText("Редактирование фигуры");
        }


        updateList();
    }



    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
            int position = viewHolder.getAdapterPosition();
            drawView.deletePointsWithFigure(position);
            JPointData.getInstance().getFigures().remove(position);
            updateList();
        }
    };



}