package com.example.marplex.schoolbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

/**
 * Created by marco on 4/22/16.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context mContext;
    private ArrayList<Voto> mVoti;

    public NotificationAdapter(Context context, ArrayList<Voto> voti){
        this.mContext = context;
        this.mVoti = voti;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.model_voto, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Voto voto = mVoti.get(position);

        holder.voto.setText(voto.voto);
        holder.voto.setBackgroundColor(Votes.getColorByVote(Votes.getVoteByString(voto.voto)));

        holder.materia.setText(riceviTesto(voto.materia, voto.data, voto.tipo));
    }

    private String riceviTesto(String materia, String data, String tipo){
        return materia+" "+data+"\n"+tipo;
    }

    @Override
    public int getItemCount() {
        return mVoti.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView voto,materia;

        NotificationViewHolder(View itemView) {
            super(itemView);
            voto = (TextView)itemView.findViewById(R.id.voto);
            materia = (TextView)itemView.findViewById(R.id.voto_descrizione);
        }
    }
}
