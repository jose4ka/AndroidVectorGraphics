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
import ru.ecostudiovl.vectorgraphics.adapter.AdapterTemplatesList;
import ru.ecostudiovl.vectorgraphics.component.BufferComponent;
import ru.ecostudiovl.vectorgraphics.component.ModeComponent;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.view.DrawView;

public class FragmentDrawer extends Fragment  implements AdapterFiguresList.FigureSelect, DrawView.DrawViewCallback {


    private View view;

    private ImageButton btnAddFigure;
    private ImageButton btnClear;
    private ImageButton btnHide;
    private ImageButton btnOverview;
    private ImageButton btnOpenTemplater;
    private ImageButton btnOpenMenu;
    private ImageButton btnSelectionMode;
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
        switchMode(ModeComponent.Mode.view);
        switchSelectionMode(ModeComponent.SelectionMode.ONE);
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
                drawView.stopDrawThread();
            }
        });

        FrameLayout frameLayout = view.findViewById(R.id.frameDraw);
        drawView = new DrawView(getContext(), this);
        frameLayout.addView(drawView);

        btnChangeMode = view.findViewById(R.id.btnChangeMode);
        btnChangeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangedMode();
            }
        });

        btnOverview = view.findViewById(R.id.btnOverview);
        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchMode(ModeComponent.Mode.view);

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
                switchMode(ModeComponent.Mode.view);
            }
        });

        btnOpenTemplater = view.findViewById(R.id.btnWorkOpenTemplateEditor);
        btnOpenTemplater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentDrawerCallback.onCreateTemplateClicked();
                drawView.stopDrawThread();
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
                drawView.stopDrawThread();
            }
        });

        btnSelectionMode = view.findViewById(R.id.btnSelectionMode);
        btnSelectionMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (ModeComponent.getInstance().getSelectionMode()) {
                    case ONE:
                        switchSelectionMode(ModeComponent.SelectionMode.MORE);
                        break;
                    case MORE:
                        switchSelectionMode(ModeComponent.SelectionMode.ONE);
                        break;

                }
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

    private void onChangedMode(){
        if (BufferComponent.getInstance().getSelectedMap().size() > 0){
            switch (ModeComponent.getInstance().getCurrentMode()) {
                case create:
                    switchMode(ModeComponent.Mode.edit);
                    break;
                case edit:
                    switchMode(ModeComponent.Mode.delete);
                    break;
                case delete:
                    switchMode(ModeComponent.Mode.create);
                    break;
            }
        }
    }


    private void switchMode(ModeComponent.Mode mode){
        ModeComponent.getInstance().setCurrentMode(mode);
        switch (ModeComponent.getInstance().getCurrentMode()) {
            case create:
                tvInfoLabel.setText("Создание точек");
                btnChangeMode.setEnabled(true);
                btnChangeMode.setVisibility(View.VISIBLE);
                btnChangeMode.setImageResource(R.drawable.ic_baseline_add_circle_24);
                btnSelectionMode.setVisibility(View.GONE);
                break;
            case edit:
                tvInfoLabel.setText("Редактирование фигуры");
                btnChangeMode.setEnabled(true);
                btnChangeMode.setVisibility(View.VISIBLE);
                btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
                btnSelectionMode.setVisibility(View.GONE);
                break;
            case delete:
                tvInfoLabel.setText("Удаление точек");
                btnChangeMode.setEnabled(true);
                btnChangeMode.setVisibility(View.VISIBLE);
                btnChangeMode.setImageResource(R.drawable.ic_baseline_delete_24);
                btnSelectionMode.setVisibility(View.GONE);
                break;
            case view:
                BufferComponent.getInstance().getSelectedMap().clear();
                tvInfoLabel.setText("Обзор");
                btnChangeMode.setEnabled(false);
                btnChangeMode.setVisibility(View.GONE);
                btnSelectionMode.setVisibility(View.VISIBLE);
                clearSelected();
                updateList();
                break;

        }

    }

    private void switchSelectionMode(ModeComponent.SelectionMode mode){
        ModeComponent.getInstance().setSelectionMode(mode);
        switch (ModeComponent.getInstance().getSelectionMode()) {
            case ONE:
                btnSelectionMode.setImageResource(R.drawable.ic_baseline_touch_app_white_24);
                if (BufferComponent.getInstance().getSelectedMap().size() > 0){
                    btnChangeMode.setVisibility(View.VISIBLE);
                }

                break;
            case MORE:
                btnSelectionMode.setImageResource(R.drawable.ic_baseline_touch_app_24);
                if (BufferComponent.getInstance().getSelectedMap().size() > 0){
                    btnChangeMode.setVisibility(View.GONE);
                }
                break;

        }

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
        switch (ModeComponent.getInstance().getSelectionMode()){
            case ONE:
                BufferComponent.getInstance().getSelectedMap().clear();
                BufferComponent.getInstance().getSelectedMap().put(index, 0);

                if (JPointData.getInstance().getFigures().get(index).getPoints().size() == 0){
                    switchMode(ModeComponent.Mode.create);
                    btnChangeMode.setImageResource(R.drawable.ic_baseline_add_circle_24);
                    tvInfoLabel.setText("Создание точек");
                }
                else {
                    switchMode(ModeComponent.Mode.edit);
                    btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
                    tvInfoLabel.setText("Редактирование фигуры");
                }

                rvFigures.getAdapter().notifyDataSetChanged();
                break;
            case MORE:
                if (BufferComponent.getInstance().getSelectedMap().containsKey(index)){
                    BufferComponent.getInstance().getSelectedMap().remove(index);
                }
                else {
                    BufferComponent.getInstance().getSelectedMap().put(index, 0);
                }

                rvFigures.getAdapter().notifyDataSetChanged();
                break;
        }


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