package ru.ecostudiovl.vectorgraphics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.ecostudiovl.vectorgraphics.R;
import ru.ecostudiovl.vectorgraphics.component.BufferComponent;
import ru.ecostudiovl.vectorgraphics.pointsystem.figures.JFigure;

public class AdapterFiguresList extends RecyclerView.Adapter<AdapterFiguresList.ViewHolder> {


    private List<JFigure> mData;
    private FigureSelect figureSelect;
    private Context context;

    public AdapterFiguresList(List<JFigure> figures, FigureSelect figureSelect, Context context){
        mData = figures;
        this.figureSelect = figureSelect;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_figure, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                figureSelect.onSelectFigure(position);
            }
        });

        if (BufferComponent.getInstance().isContainsFigure(position)){
            holder.lnIndicator.setBackgroundColor(context.getColor(R.color.selected_figure));
        }
        else {
            holder.lnIndicator.setBackgroundColor(context.getColor(R.color.simple_figure));
        }

        holder.tvName.setText(mData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        LinearLayout linearLayout;
        LinearLayout lnIndicator;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.etElementFigureName);
            linearLayout = itemView.findViewById(R.id.lnElFigureMain);
            lnIndicator = itemView.findViewById(R.id.lnElFigureIndicator);
        }
    }


    public interface FigureSelect{
        void onSelectFigure(int index);
    }
}
