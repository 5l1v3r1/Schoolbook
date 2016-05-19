package com.example.marplex.schoolbook.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.models.Materia;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

/**
 * Created by marco on 1/29/16.
 */
public class MaterieAdapter extends RecyclerView.Adapter <MaterieAdapter.materieAdapterHolder> {

    private ArrayList<Materia> materie;
    private int period;
    private MaterieAdapterInterface listener;

    public MaterieAdapter(ArrayList<Materia> modelData, int period, MaterieAdapterInterface listener) {
        this.materie = modelData;
        this.period = period;
        this.listener = listener;
    }

    @Override
    public materieAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
        inflate(R.layout.model_materia,
                viewGroup,
                false);
        return new materieAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder( materieAdapterHolder viewHolder, int position) {
        final Materia materia = materie.get(position);

        viewHolder.materia.setText(materia.testoMateria);
        viewHolder.media.setText(materia.mediaMateria+"");
        viewHolder.bar.setProgress((int)materia.mediaMateria*10);
        viewHolder.bar.setColor(riceviColore(materia.mediaMateria));

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnCardClick(materia.testoMateria);
            }
        });
    }

    int riceviColore(double val){
        if(6<=val && val<=7){
            return Color.parseColor("#ffae00"); //Giallo
        }else{
            if(val>7 && val<11){
                return Color.parseColor("#00FF66"); //Verde
            }else if(val==11) return Color.parseColor("#3F51B5"); //Blu per nav
            else if(val==12) return Color.parseColor("#00FF66"); //Verde per +
            else if(val==13) return Color.parseColor("#FF4343"); //Rosso per -
            else return Color.parseColor("#FF4343"); //Rosso
        }
    }

    String riceviTesto(String materia, String data, String tipo){
        return materia+" "+data+"\n"+tipo;
    }

    @Override
    public int getItemCount() {
        return materie.size();
    }

    public final static class materieAdapterHolder  extends RecyclerView.ViewHolder {
        TextView media,materia;
        CircularProgressBar bar;
        CardView card;

        materieAdapterHolder(View itemView) {
            super(itemView);
            bar = (CircularProgressBar) itemView.findViewById(R.id.pbar_materiaPrimo);
            media = (TextView)itemView.findViewById(R.id.mediaMateria);
            materia = (TextView)itemView.findViewById(R.id.txt_materiaPrimoperiodo);
            card = (CardView)itemView.findViewById(R.id.pbar_materiaSecondo);
        }
    }
    public interface MaterieAdapterInterface{
        void OnCardClick(String materia);
    }
}
