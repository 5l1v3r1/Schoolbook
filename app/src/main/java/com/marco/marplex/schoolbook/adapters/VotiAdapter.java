package com.marco.marplex.schoolbook.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.List;

/**
 * Created by marco on 1/29/16.
 */
public class VotiAdapter extends RecyclerView.Adapter <VotiAdapter.votiAdapterHolder>{

    private List<Voto> voti;

    public VotiAdapter(List<Voto> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException(
                    "voti must not be null");
        }
        this.voti = modelData;
    }

    @Override
    public votiAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.model_voto,
                        viewGroup,
                        false);
        return new votiAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder( votiAdapterHolder viewHolder, int position) {
        Voto voto = voti.get(position);

        viewHolder.voto.setText(voto.voto);
        if(voto.special && !voto.voto.equals("-") && !voto.voto.equals("+")){
            viewHolder.voto.setBackgroundColor(Color.parseColor("#3F51B5")); //Blu
        } else viewHolder.voto.setBackgroundColor(Votes.getColorByVote(Votes.getVoteByString(voto.voto)));

        viewHolder.materia.setText(riceviTesto(voto.materia, voto.data, voto.tipo));
    }

    private String riceviTesto(String materia, String data, String tipo){
        return materia+" "+data+"\n"+tipo;
    }

    @Override
    public int getItemCount() {
        return voti.size();
    }

    public final static class votiAdapterHolder  extends RecyclerView.ViewHolder {
        TextView voto,materia;

        votiAdapterHolder(View itemView) {
            super(itemView);
            voto = (TextView)itemView.findViewById(R.id.voto);
            materia = (TextView)itemView.findViewById(R.id.voto_descrizione);
        }
    }
}