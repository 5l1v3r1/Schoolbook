package com.example.marplex.schoolbook.adapters;

/**
 * Created by marco on 2/16/16.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.models.Evento;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {

    public CaldroidSampleCustomAdapter(Context context, int month, int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }

        TextView date = (TextView) cellView.findViewById(R.id.date);

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        if(((ArrayList<Evento>)extraData.get("eventi"))==null){
            date.setTextColor(Color.BLACK);
            date.setBackgroundColor(Color.parseColor("#FFFFFF"));
            date.setText("" + dateTime.getDay());

            cellView.setPadding(leftPadding, topPadding, rightPadding,
                    bottomPadding);

            setCustomResources(dateTime, cellView, date);
            return cellView;
        }else {
            ArrayList<Evento> eventi = (ArrayList<Evento>) extraData.get("eventi");
            Evento evento = null;
            for(int i=0; i< eventi.size(); i++){
                Evento evento1 = eventi.get(i);
                StringTokenizer tokenizer = new StringTokenizer(evento1.data, "-");
                String anno = tokenizer.nextToken();
                String mese = tokenizer.nextToken();
                String giorno = tokenizer.nextToken();
                if(dateTime.getDay().equals(Integer.parseInt(giorno)) && dateTime.getMonth().equals(Integer.parseInt(mese)) && dateTime.getYear().equals(Integer.parseInt(anno))){
                    evento = evento1;
                }
            }
            if(evento==null){
                date.setTextColor(Color.BLACK);
                date.setBackgroundColor(Color.parseColor("#FFFFFF"));
                date.setText("" + dateTime.getDay());
                cellView.setPadding(leftPadding, topPadding, rightPadding,
                        bottomPadding);

                setCustomResources(dateTime, cellView, date);
                return cellView;
            }


            StringTokenizer tokenizer = new StringTokenizer(evento.data, "-");
            String anno = tokenizer.nextToken();
            String mese = tokenizer.nextToken();
            String giorno = tokenizer.nextToken();

            if (dateTime.getYear() == Integer.parseInt(anno) && dateTime.getMonth() == Integer.parseInt(mese) && dateTime.getDay() == Integer.parseInt(giorno)) {
                date.setTextColor(Color.WHITE);
                date.setBackgroundResource(R.drawable.circle);
            } else {
                date.setTextColor(Color.BLACK);
                date.setBackgroundColor(Color.parseColor("#EEEEEE"));
            }

            date.setText("" + dateTime.getDay());

            cellView.setPadding(leftPadding, topPadding, rightPadding,
                    bottomPadding);

            setCustomResources(dateTime, cellView, date);

            return cellView;
        }
    }

}

