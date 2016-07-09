package com.marco.marplex.schoolbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Evento;

import java.util.List;

/**
 * Created by marco on 5/8/16.
 */
public class EventAdapter extends ArrayAdapter<Evento> {
    private int resource;
    public EventAdapter(Context context, int resource, List<Evento> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null){
         view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        ((TextView) view.findViewById(R.id.prof)).setText(getItem(position).autore);
        ((TextView) view.findViewById(R.id.text)).setText(getItem(position).testo);

        return view;
    }
}
