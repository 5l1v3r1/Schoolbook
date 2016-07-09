package com.marco.marplex.schoolbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Note;

import java.util.ArrayList;

/**
 * Created by marco on 5/24/16.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private ArrayList<Note> mList;
    private Context mContext;

    public NoteAdapter(ArrayList<Note> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public NoteAdapter(ArrayList<Note> mList) {
        this.mList = mList;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.model_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.mProf.setText(mList.get(position).prof);
        holder.mDate.setText(mList.get(position).data);
        holder.mNote.setText(mList.get(position).nota);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static final class NoteViewHolder extends RecyclerView.ViewHolder{

        public TextView mProf, mDate, mNote;

        public NoteViewHolder(View itemView) {
            super(itemView);

            mProf = (TextView) itemView.findViewById(R.id.txt_note_prof);
            mNote = (TextView) itemView.findViewById(R.id.txt_note_nota);
            mDate = (TextView) itemView.findViewById(R.id.txt_note_date);
        }
    }
}
