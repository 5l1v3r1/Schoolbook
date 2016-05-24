package com.marco.marplex.schoolbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Comunication;

import java.util.ArrayList;

/**
 * Created by marco on 5/24/16.
 */

public class ComunicationAdapter extends RecyclerView.Adapter<ComunicationAdapter.ComunicationViewHolder>{

    private ArrayList<Comunication> mList;
    private Context mContext;

    public ComunicationAdapter(ArrayList<Comunication> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public ComunicationAdapter(ArrayList<Comunication> mList) {
        this.mList = mList;
    }

    @Override
    public ComunicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.model_comunication, parent, false);
        return new ComunicationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ComunicationViewHolder holder, int position) {
        holder.mTitle.setText(mList.get(position).title);
        holder.mDate.setText(mList.get(position).date);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static final class ComunicationViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitle, mDate;

        public ComunicationViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.txt_circ_title);
            mDate = (TextView) itemView.findViewById(R.id.txt_circ_date);
        }
    }
}
