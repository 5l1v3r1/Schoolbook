package com.example.marplex.schoolbook.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.models.Voto;

import java.util.List;

/**
 * Created by marco on 1/29/16.
 */
public class votiAdapter extends RecyclerView.Adapter <votiAdapter.votiAdapterHolder> {

    private List<Voto> voti;

    public votiAdapter(List<Voto> modelData) {
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
        viewHolder.voto.setBackgroundColor(riceviColore(getVoto(voto.voto)));

        viewHolder.materia.setText(riceviTesto(voto.materia, voto.data, voto.tipo));
    }

    double getVoto(String val){

        double value = 0.0;
        if(val.length()==1){
            try {
                return Integer.parseInt(val);
            }catch(Exception e){
                if(val=="+") return 12;
                else if(val=="-") return 13;
            }
        }
        else {
            if(val=="nav") return 11;
            else{
                value = Integer.parseInt(val.substring(0,1));
                if(val.endsWith("-")) return value - 0.15;
                else if(val.endsWith("+")) return value + 0.15;
                else if(val.endsWith("½")) return value + 0.5;
            }

        }return 0;
    }

    int riceviColore(double val){
        if(6<=val && val<=7){
            return Color.parseColor("#ffae00"); //Giallo
        }else{
            if(val>7 && val<=10){
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
