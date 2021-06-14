package ru.ecostudiovl.vectorgraphics.fragment.constructor;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.adapter.AdapterTemplatesList;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;


public class FragmentTemplateEditor extends Fragment  implements AdapterTemplatesList.TemplateSelect {


    private View view;//Корневой view элемент фрагмента
    private RecyclerView rvTemplates; //Элемент отображающий все имеющиеся шаблоны
    private EditText etTemplateName; //Поле ввода имени нового шаблона
    private EditText etPointsNumber; //Поле ввода количества точек шаблона
    private CheckBox cbIsClosedFigure; //Чек-бокс, закрытая ли фигура
    private CheckBox cbIsClosedFigureNumber;//Чек-бокс, закрытое ли кол-во точек

    private int selectedIndex = 0; //Индекс выбранного шаблона для удаления

    //Интерфейс коллбэка для активности
    public interface FragmentTemplateEditorCallback{
        void onBackPressedTemplate();
    }

    //Объект интерфейса коллбэка чтобы к нему обратиться
    private FragmentTemplateEditorCallback fragmentTemplateEditorCallback;

    public static FragmentTemplateEditor newInstance() {
        return new FragmentTemplateEditor();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentTemplateEditorCallback = (FragmentTemplateEditorCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_template_editor, container, false);
        initializeViewElements();
        updateList();
        return view;
    }

    private void initializeViewElements(){
        rvTemplates = view.findViewById(R.id.rvTemplates);
        etTemplateName = view.findViewById(R.id.tvTemplateName);
        etPointsNumber = view.findViewById(R.id.tvTemplatePointsCount);
        etPointsNumber.setEnabled(false);

        cbIsClosedFigure = view.findViewById(R.id.cbIsClosedNumber);
        cbIsClosedFigureNumber = view.findViewById(R.id.cbIsClosedPontsNumber);
        cbIsClosedFigureNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etPointsNumber.setEnabled(isChecked);

            }
        });

        ImageButton btnCreateTemplate = view.findViewById(R.id.btnActTempCreateTemplate);
        btnCreateTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbIsClosedFigureNumber.isChecked() && !etPointsNumber.getText().toString().isEmpty()){
                    int pointsNumber = Integer.parseInt(etPointsNumber.getText().toString());
                    JPointData.getInstance().getTemplates().add(new JFigureTemplates(
                            pointsNumber,
                            cbIsClosedFigureNumber.isChecked(),
                            cbIsClosedFigure.isChecked(),
                            etTemplateName.getText().toString()

                    ));
                }
                else {
                    JPointData.getInstance().getTemplates().add(new JFigureTemplates(
                            0,
                            cbIsClosedFigureNumber.isChecked(),
                            cbIsClosedFigure.isChecked(),
                            etTemplateName.getText().toString()

                    ));
                }

                updateList();

            }
        });


        ImageButton btnBack = view.findViewById(R.id.btnActTempBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTemplateEditorCallback.onBackPressedTemplate();
            }
        });
    }


    @Override
    public void onSelectTemplate(int index) {
        Objects.requireNonNull(rvTemplates.getAdapter()).notifyDataSetChanged();
    }



    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            selectedIndex = -1;
            int position = viewHolder.getAdapterPosition();
            JPointData.getInstance().getTemplates().remove(position);
            updateList();
        }
    };


    private void updateList(){
        simpleItemTouchCallback.setDefaultSwipeDirs(ItemTouchHelper.UP | ItemTouchHelper.DOWN);

        AdapterTemplatesList adapterTemplatesList = new AdapterTemplatesList(this, requireContext(), selectedIndex);
        rvTemplates.setAdapter(adapterTemplatesList);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        rvTemplates.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvTemplates);


    }
}