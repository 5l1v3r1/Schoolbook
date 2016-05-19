package com.marco.marplex.schoolbook.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Compare;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

/**
 * Created by marco on 4/24/16.
 */
public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.CompareViewHolder> {

    private Context mContext;
    private ArrayList<Compare> mList;

    public CompareAdapter(Context context, ArrayList<Compare>list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public CompareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.model_compare, parent, false);
        return new CompareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompareViewHolder holder, int position) {
        holder.mTextViewPrimo.setText(mList.get(position).materia1.testoMateria);
        holder.mTextViewSecondo.setText(mList.get(position).materia2.testoMateria);

        holder.mMediaMateria1.setText(mList.get(position).materia1.mediaMateria+"");
        holder.mMediaMateria2.setText(mList.get(position).materia2.mediaMateria+"");

        holder.mProgressPrimo.setProgress((float) mList.get(position).materia1.mediaMateria*10);
        holder.mProgressPrimo.setColor(riceviColore(mList.get(position).materia1.mediaMateria));
        holder.mProgressSecondo.setProgress((float) mList.get(position).materia2.mediaMateria*10);
        holder.mProgressSecondo.setColor(riceviColore(mList.get(position).materia2.mediaMateria));
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

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class CompareViewHolder extends RecyclerView.ViewHolder{

        TextView mMediaMateria1;
        TextView mMediaMateria2;
        TextView mTextViewPrimo;
        TextView mTextViewSecondo;
        CircularProgressBar mProgressPrimo;
        CircularProgressBar mProgressSecondo;

        CompareViewHolder(View view){
            super(view);

            mMediaMateria1 = (TextView) view.findViewById(R.id.txt_mediaMateria);
            mMediaMateria2 = (TextView) view.findViewById(R.id.txt_mediaMateria2);
            mTextViewPrimo = (TextView) view.findViewById(R.id.txt_materiaPrimoperiodo);
            mTextViewSecondo = (TextView) view.findViewById(R.id.txt_materiaSecondoperiodo);

            mProgressPrimo = (CircularProgressBar) view.findViewById(R.id.pbar_materiaPrimo);
            mProgressSecondo = (CircularProgressBar) view.findViewById(R.id.pbar_materiaSecondo);
        }
    }
}
