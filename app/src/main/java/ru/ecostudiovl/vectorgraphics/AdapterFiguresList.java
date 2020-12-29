package ru.ecostudiovl.vectorgraphics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

import ru.ecostudiovl.vectorgraphics.figure.Figure;

public class AdapterFiguresList extends RecyclerView.Adapter<AdapterFiguresList.ViewHolder> {


    private List<Figure> mData;
    private FigureSelect figureSelect;

    public AdapterFiguresList(List<Figure> figures, FigureSelect figureSelect){
        mData = figures;
        this.figureSelect = figureSelect;
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
        holder.tvName.setText(mData.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.etElementFigureName);
            linearLayout = itemView.findViewById(R.id.lnElFigureMain);
        }
    }


    public interface FigureSelect{
        void onSelectFigure(int index);
    }
}
