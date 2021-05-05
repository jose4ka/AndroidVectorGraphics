package ru.ecostudiovl.vectorgraphics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.pointsystem.JPointData;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;
import ru.ecostudiovl.vectorgraphics.pointsystem.template.JFigureTemplates;

public class AdapterTemplatesList extends RecyclerView.Adapter<AdapterTemplatesList.ViewHolder> {


    private List<JFigureTemplates> mData;
    private TemplateSelect figureSelect;
    private Context context;
    private int selectedTemplate;

    public AdapterTemplatesList(TemplateSelect figureSelect, Context context, int selectedTemplate){
        mData = JPointData.getInstance().getTemplates();
        this.figureSelect = figureSelect;
        this.context = context;
        this.selectedTemplate = selectedTemplate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_template, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                figureSelect.onSelectTemplate(position);
            }
        });

        if (position == selectedTemplate){
            holder.lnIndicator.setBackgroundColor(context.getResources().getColor(R.color.selected_figure));
        }
        else {
            holder.lnIndicator.setBackgroundColor(context.getResources().getColor(R.color.simple_figure));
        }

        holder.tvName.setText(mData.get(position).getName());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                figureSelect.onDeletedTemlate(position);
            }
        });

        holder.tvPointCount.setText(""+mData.get(position).getPointsCount());
        holder.lnCLosedIndicator.setBackground(mData.get(position).isClosedFigure() ? context.getDrawable(R.drawable.ic_baseline_all_inclusive_24) : context.getDrawable(R.drawable.ic_baseline_multiline_chart_24));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvPointCount;
        LinearLayout lnCLosedIndicator;
        TextView tvName;
        LinearLayout linearLayout;
        LinearLayout lnIndicator;
        ImageButton btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.etElementFigureName);
            linearLayout = itemView.findViewById(R.id.lnElFigureMain);
            btnDelete = itemView.findViewById(R.id.btnDeleteFigure);
            tvPointCount = itemView.findViewById(R.id.tvTemplatePointsCount);
            lnCLosedIndicator = itemView.findViewById(R.id.lnFigureTypeIsClosed);
            lnIndicator = itemView.findViewById(R.id.lnElTemplateIndicator);
        }
    }


    public interface TemplateSelect{
        void onSelectTemplate(int index);
        void onDeletedTemlate(int index);
    }
}
