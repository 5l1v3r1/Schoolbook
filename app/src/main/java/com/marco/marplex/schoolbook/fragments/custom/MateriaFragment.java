package com.marco.marplex.schoolbook.fragments.custom;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.marco.marplex.schoolbook.ChartActivity;
import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.adapters.MaterieAdapter;
import com.marco.marplex.schoolbook.adapters.VotesDialogAdapter;
import com.marco.marplex.schoolbook.models.Materia;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.MathUtils;
import com.marco.marplex.schoolbook.utilities.Subjects;
import com.marco.marplex.schoolbook.utilities.Votes;
import com.marco.marplex.schoolbook.views.GridRecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MateriaFragment extends PagerFragment {

    private GridRecyclerView materieRecyclerList;
    private TextView mediaTotaleMaterie;

    protected int mPeriod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_materia_first_period, container, false);

        materieRecyclerList = (GridRecyclerView) rootView.findViewById(R.id.materieList);
        mediaTotaleMaterie = (TextView) rootView.findViewById(R.id.mediaTotaleMaterie);

        materieRecyclerList.setHasFixedSize(true);
        materieRecyclerList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        materieRecyclerList.scheduleLayoutAnimation();

        init();
        setScrollListener();
        populateList();

        return rootView;
    }

    @Override
    public void showChart(){
        Intent i = new Intent(getActivity(), ChartActivity.class);
        i.putExtra("period", mPeriod);

        startActivity(i);
    }

    public void populateList(){

        ArrayList<Materia> mList = Subjects.getSubjects(getContext(), mPeriod);

        MaterieAdapter adapter = new MaterieAdapter(mList, mPeriod, new MaterieAdapter.MaterieAdapterInterface() {

            @Override
            public void OnCardClick(View card, View progress, String materia) {
                viewVotesList(mPeriod, materia);
            }
        });

        materieRecyclerList.setAdapter(adapter);

        double defSum = 0;
        int NaNNumbers = 0;
        for (Materia item : mList){
            if(item.mediaMateria>0) {
                defSum += item.mediaMateria;
            }else NaNNumbers ++;
        }
        mediaTotaleMaterie.setText("Media totale: " + MathUtils.rintRound( defSum / ( mList.size() - NaNNumbers ), 2));
    }

    private void setScrollListener(){
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            boolean hideView = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (hideView) {
                    mediaTotaleMaterie.setVisibility(View.GONE);
                } else {
                    mediaTotaleMaterie.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 5) {
                    hideView = true;
                } else if (dy < -5) {
                    hideView = false;
                }
            }
        };

        materieRecyclerList.setOnScrollListener(onScrollListener);
    }

    private Dialog viewVotesList(int periodo, String materia){
        final Dialog mBottomSheetDialog = new Dialog (getActivity(),R.style.MaterialDialogSheet);
        ListView list = new ListView(getContext());
        list.setDivider(null);
        list.setBackgroundColor(Color.parseColor("#EEEEEE"));

        ArrayList<Voto> votes = Votes.getVotesByMateria(getContext(), materia, periodo);
        list.setAdapter(new VotesDialogAdapter(getContext(),R.layout.model_voto, votes));

        mBottomSheetDialog.setContentView(list);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        return mBottomSheetDialog;
    }

    public abstract void init();

}
