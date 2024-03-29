package ru.ecostudiovl.vectorgraphics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class AdapterTemplatesList extends RecyclerView.Adapter<AdapterTemplatesList.ViewHolder> {


    private List<JFigureTemplates> mData; //Локальный список шаблонов для отображения
    private TemplateSelect figureSelect; //Объект интерфейса колбэка
    private Context context; //Контекст в котором работает данный адаптер
    private int selectedTemplate; //Выбранный шаблон, для посветки

    //Конструктор адаптера
    public AdapterTemplatesList(TemplateSelect figureSelect, Context context, int selectedTemplate){
        mData = JPointData.getInstance().getTemplates();
        this.figureSelect = figureSelect;
        this.context = context;
        this.selectedTemplate = selectedTemplate;
    }

    //Интерфейс колбэка выбранной фигуры
    public interface TemplateSelect{
        void onSelectTemplate(int index);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_template, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                figureSelect.onSelectTemplate(position);
            }
        });

        if (position == selectedTemplate){
            holder.lnIndicator.setBackgroundColor(context.getColor(R.color.selected_figure));
        }
        else {
            holder.lnIndicator.setBackgroundColor(context.getColor(R.color.simple_figure));
        }

        holder.tvName.setText(mData.get(position).getName());


        holder.tvPointCount.setText(""+mData.get(position).getPointsCount());
        holder.lnCLosedIndicator.setBackground(mData.get(position).isClosedFigure() ? context.getDrawable(R.drawable.ic_baseline_all_inclusive_24) : context.getDrawable(R.drawable.ic_baseline_multiline_chart_24));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvPointCount;
        LinearLayout lnCLosedIndicator;
        TextView tvName;
        CardView linearLayout;
        LinearLayout lnIndicator;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.etElementFigureName);
            linearLayout = itemView.findViewById(R.id.lnElFigureMain);
            tvPointCount = itemView.findViewById(R.id.tvTemplatePointsCount);
            lnCLosedIndicator = itemView.findViewById(R.id.lnFigureTypeIsClosed);
            lnIndicator = itemView.findViewById(R.id.lnElTemplateIndicator);
        }
    }


    public void setSelectedTemplate(int selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }
}
