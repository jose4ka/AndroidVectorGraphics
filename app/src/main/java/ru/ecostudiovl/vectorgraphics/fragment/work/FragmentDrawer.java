package ru.ecostudiovl.vectorgraphics.fragment.work;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.TreeMap;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterFiguresList;
import ru.ecostudiovl.vectorgraphics.component.BufferComponent;
import ru.ecostudiovl.vectorgraphics.component.ModeComponent;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.view.DrawView;

public class FragmentDrawer extends Fragment  implements AdapterFiguresList.FigureSelect, DrawView.DrawViewCallback {


    private View view; //Корневой view элемент фрагмента

    private ImageButton btnHide; //Кнопка скрытия списка фигур
    private ImageButton btnSelectionMode; //Кнопка режима выбора фигуры
    private ImageButton btnDeleteSelection; //Кнопка удаления выбраных фигур
    private ImageButton btnCopy; //Кнопка копирования фигуры
    private ImageButton btnMove; //Кнопка перемещения фигуры

    private CardView lnList; //Обхект на экране со списком фигур
    private RecyclerView rvFigures; //Список фигур
    private DrawView drawView; //Элемент отображающий сами фигуры на экране (линии и точки)
    private ImageButton btnChangeMode; //Кнопка смены режима рисования
    private TextView tvInfoLabel; //Информационное сообщение о текущих действияъ

    private LinearLayout lnProperties; //Не используется
    private SeekBar sbScale, sbRotate; //Не используются

    //Интерфейс для коллбэка в активность
    public interface FragmentDrawerCallback{
        void onCreateFigureClicked();
        void onCreateTemplateClicked();
        void onMainMenuClicked();
    }

    //Объект интерфейса, для обращения к нему
    private FragmentDrawerCallback fragmentDrawerCallback;

    public FragmentDrawer() {
        // Required empty public constructor
    }


    public static FragmentDrawer newInstance() {
        return new FragmentDrawer();
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
        switchMode(ModeComponent.Mode.VIEW);
        switchSelectionMode(ModeComponent.SelectionMode.ONE);
        return view;
    }

    /*
    Процедура инициализирует все view-элементы этого фрагмента
     */
    private void initializeViewElements(){
        rvFigures = view.findViewById(R.id.rvFiguresList);
        tvInfoLabel = view.findViewById(R.id.tvSelectedMode);
        ImageButton btnAddFigure = view.findViewById(R.id.btnAddFigure);
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

        ImageButton btnOverview = view.findViewById(R.id.btnOverview);
        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchMode(ModeComponent.Mode.VIEW);

            }
        });

        ImageButton btnClear = view.findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JPointData.getInstance().getPoints().clear();
                JPointData.getInstance().getFigures().clear();
                updateList();
                tvInfoLabel.setText("Холст очищен!");
                switchMode(ModeComponent.Mode.VIEW);
            }
        });

        ImageButton btnOpenTemplater = view.findViewById(R.id.btnWorkOpenTemplateEditor);
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
                int visibility = lnList.getVisibility();
                if (visibility == View.VISIBLE) {
                    lnList.setVisibility(View.GONE);
                    btnHide.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
                } else if (visibility == View.GONE) {
                    lnList.setVisibility(View.VISIBLE);
                    btnHide.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
                }
            }
        });

        ImageButton btnOpenMenu = view.findViewById(R.id.btnWorkGoToMenu);
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
                    default:
                        break;

                }
            }
        });


        btnDeleteSelection = view.findViewById(R.id.btnDeleteSelected);
        btnDeleteSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int size = BufferComponent.getInstance().getSelectedMap().size();
                int i = 0;
                while (i < size) {
                    boolean flag = false;
                    int lastInd = -1;
                    for (TreeMap.Entry<Integer, Integer> entry:BufferComponent.getInstance().getSelectedMap().entrySet()) {
                        if (!flag){
                            lastInd = entry.getKey();

                            drawView.deletePointsWithFigure(lastInd);
                            JPointData.getInstance().getFigures().remove(lastInd);

                            flag = true;
                        }
                    }

                    if (lastInd != -1){
                        BufferComponent.getInstance().deleteFigure(lastInd);
                        BufferComponent.getInstance().minimize(lastInd);
                    }
                    i++;
                }


                BufferComponent.getInstance().clearSelected();
                switchMode(ModeComponent.Mode.VIEW);
                updateList();
            }
        });

        drawView.setScaleMultiplier(50);
        ImageButton btnLeft = view.findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setxDelta(drawView.getxDelta() + 100);
            }
        });

        ImageButton btnRight = view.findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setxDelta(drawView.getxDelta() - 100);
            }
        });

        ImageButton btnUp = view.findViewById(R.id.btnUp);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setyDelta(drawView.getyDelta() - 100);
            }
        });

        ImageButton btnDown = view.findViewById(R.id.btnDown);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setyDelta(drawView.getyDelta() + 100);
            }
        });

        btnCopy = view.findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchMode(ModeComponent.Mode.COPY);
            }
        });

        btnMove = view.findViewById(R.id.btnMove);
        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ModeComponent.getInstance().getCurrentMode()){
                    case MOVE:
                        switchMode(ModeComponent.Mode.EDIT);
                        break;
                    default:
                        switchMode(ModeComponent.Mode.MOVE);
                        break;
                }

            }
        });

        lnProperties = view.findViewById(R.id.lnFigureProportions);

        sbRotate = view.findViewById(R.id.sbRotate);
        sbRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((float) progress > JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).getRotate()){
                    JPointData.getInstance().rotateFigurePlus(BufferComponent.getInstance().getCurrentSelectedObject(), (float) progress);
                }
                else {
                    JPointData.getInstance().rotateFigureMinus(BufferComponent.getInstance().getCurrentSelectedObject(), (float) progress);
                }
                JPointData.getInstance().getFigures().get(BufferComponent.getInstance().getCurrentSelectedObject()).setRotate((float) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    /*
    Процедура, которая меняет режим рабоыт программы, на основе текущего режима
     */
    private void onChangedMode(){
        if (BufferComponent.getInstance().hasSelectedFigures()){
            switch (ModeComponent.getInstance().getCurrentMode()) {
                case CREATE:
                    switchMode(ModeComponent.Mode.EDIT);
                    break;
                case EDIT:
                    switchMode(ModeComponent.Mode.DELETE);
                    break;
                case DELETE:
                    switchMode(ModeComponent.Mode.CREATE);
                    break;
                case MOVE:
                case COPY:
                    switchMode(ModeComponent.Mode.EDIT);
                    break;
                default:
                    break;
            }
        }
    }

    /*
    Процедура которая меняет текущий режим работы программы
    и подгоняет интерфейс под него
    НА вход подаётся новый режим работы программы
     */
    private void switchMode(ModeComponent.Mode mode){
        ModeComponent.getInstance().setCurrentMode(mode);
        switch (ModeComponent.getInstance().getCurrentMode()) {
            case CREATE:
                tvInfoLabel.setText("Создание точек");
                btnChangeMode.setEnabled(true);
                btnChangeMode.setVisibility(View.VISIBLE);
                btnChangeMode.setImageResource(R.drawable.ic_baseline_add_circle_24);
                btnSelectionMode.setVisibility(View.GONE);
                btnDeleteSelection.setVisibility(View.GONE);
                btnCopy.setVisibility(View.GONE);
                btnMove.setVisibility(View.GONE);
                lnProperties.setVisibility(View.VISIBLE);
                break;
            case EDIT:
                tvInfoLabel.setText("Редактирование фигуры");
                btnChangeMode.setEnabled(true);
                btnChangeMode.setVisibility(View.VISIBLE);
                btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
                btnSelectionMode.setVisibility(View.GONE);
                btnDeleteSelection.setVisibility(View.GONE);
                btnCopy.setVisibility(View.VISIBLE);
                btnMove.setVisibility(View.VISIBLE);
                lnProperties.setVisibility(View.VISIBLE);
                break;
            case DELETE:
                tvInfoLabel.setText("Удаление точек");
                btnChangeMode.setEnabled(true);
                btnChangeMode.setVisibility(View.VISIBLE);
                btnChangeMode.setImageResource(R.drawable.ic_baseline_delete_24);
                btnSelectionMode.setVisibility(View.GONE);
                btnDeleteSelection.setVisibility(View.GONE);
                btnCopy.setVisibility(View.GONE);
                btnMove.setVisibility(View.GONE);
                lnProperties.setVisibility(View.VISIBLE);
                break;
            case VIEW:
                BufferComponent.getInstance().clearSelected();
                tvInfoLabel.setText("Обзор");
                btnChangeMode.setEnabled(false);
                btnChangeMode.setVisibility(View.GONE);
                btnSelectionMode.setVisibility(View.VISIBLE);
                btnDeleteSelection.setVisibility(View.GONE);
                btnCopy.setVisibility(View.GONE);
                btnMove.setVisibility(View.GONE);
                lnProperties.setVisibility(View.GONE);
                updateList();
                break;
            case COPY:
                tvInfoLabel.setText("Копирование");
                btnChangeMode.setVisibility(View.VISIBLE);
                btnSelectionMode.setVisibility(View.GONE);
                btnDeleteSelection.setVisibility(View.GONE);
                lnProperties.setVisibility(View.VISIBLE);
                break;
            case MOVE:
                tvInfoLabel.setText("Перемещение");
                btnChangeMode.setVisibility(View.VISIBLE);
                btnSelectionMode.setVisibility(View.GONE);
                btnDeleteSelection.setVisibility(View.GONE);
                lnProperties.setVisibility(View.VISIBLE);
                break;
        }
    }

    /*
    Процедура смены режима выбора фигур
    На вход подаётся новый режим выбора фигуры
     */
    private void switchSelectionMode(ModeComponent.SelectionMode mode){
        ModeComponent.getInstance().setSelectionMode(mode);
        ModeComponent.SelectionMode selectionMode = ModeComponent.getInstance().getSelectionMode();
        if (selectionMode == ModeComponent.SelectionMode.ONE) {
            tvInfoLabel.setText("Выделение одной фигуры");
            btnSelectionMode.setImageResource(R.drawable.ic_baseline_touch_app_white_24);
            if (BufferComponent.getInstance().hasSelectedFigures()) {
                btnChangeMode.setVisibility(View.VISIBLE);
            }
        } else if (selectionMode == ModeComponent.SelectionMode.MORE) {
            tvInfoLabel.setText("Множественное выделение");
            btnSelectionMode.setImageResource(R.drawable.ic_baseline_touch_app_24);
            if (BufferComponent.getInstance().hasSelectedFigures()) {
                btnChangeMode.setVisibility(View.GONE);
            }
        }

    }

    /*
    Процедура обновляет список фигур на экране
     */
    public void updateList(){

        simpleItemTouchCallback.setDefaultSwipeDirs(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        AdapterFiguresList adapterFiguresList = new AdapterFiguresList(JPointData.getInstance().getFigures(), this, requireContext());
        rvFigures.setAdapter(adapterFiguresList);
        rvFigures.setLayoutManager(new LinearLayoutManager(requireContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvFigures);
    }


    /*
    Процедура колбэка адаптера списка фигур
    Срабатывает при выборе фигуры в списке
    НА вход подаётся индекс выбранной фигуры
     */
    @Override
    public void onSelectFigure(int index) {
        btnDeleteSelection.setVisibility(View.VISIBLE);
        ModeComponent.SelectionMode selectionMode = ModeComponent.getInstance().getSelectionMode();

        if (selectionMode == ModeComponent.SelectionMode.ONE) {
            BufferComponent.getInstance().clearSelected();
            BufferComponent.getInstance().addFigure(index);


            if (JPointData.getInstance().getFigures().get(index).getPoints().isEmpty()) {
                switchMode(ModeComponent.Mode.CREATE);
                btnChangeMode.setImageResource(R.drawable.ic_baseline_add_circle_24);
                tvInfoLabel.setText("Создание точек");
            } else {
                switchMode(ModeComponent.Mode.EDIT);
                btnChangeMode.setImageResource(R.drawable.ic_baseline_edit_24);
                tvInfoLabel.setText("Редактирование фигуры");
            }

            Objects.requireNonNull(rvFigures.getAdapter()).notifyDataSetChanged();
        } else if (selectionMode == ModeComponent.SelectionMode.MORE) {
            if (BufferComponent.getInstance().isContainsFigure(index)) {
                BufferComponent.getInstance().deleteFigure(index);
            } else {
                BufferComponent.getInstance().addFigure(index);
            }

            Objects.requireNonNull(rvFigures.getAdapter()).notifyDataSetChanged();
        }


    }

    /*
    Колбэк от графического элемента, гвоорящий что фигуры была скопирвоана
     */
    @Override
    public void onCopyFigure() {
        switchMode(ModeComponent.Mode.EDIT);
        updateList();
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView

            int position = viewHolder.getAdapterPosition();

            if (BufferComponent.getInstance().isContainsFigure(position)){
                BufferComponent.getInstance().deleteFigure(position);
                BufferComponent.getInstance().minimize(position);
            }

            drawView.deletePointsWithFigure(position);
            JPointData.getInstance().getFigures().remove(position);

            updateList();
        }
    };




}