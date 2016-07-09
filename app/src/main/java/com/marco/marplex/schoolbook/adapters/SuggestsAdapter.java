package com.marco.marplex.schoolbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Suggest;

import java.util.ArrayList;

/**
 * Created by marco on 30/06/16.
 */

public class SuggestsAdapter extends RecyclerView.Adapter<SuggestsAdapter.ViewHolder>{

    private ArrayList<Suggest> mList;
    private Context context;

    public SuggestsAdapter(ArrayList<Suggest> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.model_suggest, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Suggest suggest = mList.get(position);

        holder.suggest.setText("\""+suggest.suggest+"\"");
        holder.icon.setImageResource(suggest.icon);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView suggest;

        public ViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.img_model_suggest);
            suggest = (TextView) itemView.findViewById(R.id.txt_second_model_suggest);
        }
    }

}
