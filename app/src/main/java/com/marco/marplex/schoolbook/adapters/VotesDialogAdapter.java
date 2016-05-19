package com.marco.marplex.schoolbook.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.List;

/**
 * Created by marco on 5/19/16.
 */
public class VotesDialogAdapter extends ArrayAdapter<Voto> {
    public VotesDialogAdapter(Context context, int resource, List<Voto> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) convertView = LayoutInflater.from(getContext())
                .inflate(R.layout.model_voto, parent, false);

        TextView voto = (TextView) convertView.findViewById(R.id.voto);
        TextView desc = (TextView) convertView.findViewById(R.id.voto_descrizione);

        voto.setText(getItem(position).voto);
        if(getItem(position).special && !getItem(position).voto.equals("-") && !getItem(position).voto.equals("+")){
            voto.setBackgroundColor(Color.parseColor("#3F51B5")); //Blu
        } else voto.setBackgroundColor(Votes.getColorByVote(Votes.getVoteByString(getItem(position).voto)));

        desc.setText(riceviTesto(getItem(position).materia, getItem(position).data, getItem(position).tipo));

        return convertView;
    }

    private String riceviTesto(String materia, String data, String tipo){
        return materia+" "+data+"\n"+tipo;
    }
}
