package com.marco.marplex.schoolbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Argument;

import java.util.List;

/**
 * Created by marco on 30/06/16.
 */

public class ArgumentsActivityAdapter extends ArrayAdapter<Argument>{

    private int resource;

    public ArgumentsActivityAdapter(Context context, int resource, List<Argument> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(resource, parent, false);
        }

        TextView teacher, date, content;
        teacher = (TextView) convertView.findViewById(R.id.txt_argument_teacher);
        date = (TextView) convertView.findViewById(R.id.txt_argument_date);
        content = (TextView) convertView.findViewById(R.id.txt_argument_content);

        teacher.setText(getItem(position).teacher);
        content.setText(getItem(position).content);
        date.setText(getItem(position).date);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_bottom);
        convertView.startAnimation(animation);
        return convertView;
    }
}
