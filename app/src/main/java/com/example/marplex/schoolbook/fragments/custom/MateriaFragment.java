package com.example.marplex.schoolbook.fragments.custom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.MaterieAdapter;
import com.example.marplex.schoolbook.models.Materia;
import com.example.marplex.schoolbook.utilities.MathUtils;
import com.example.marplex.schoolbook.utilities.Subjects;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MateriaFragment extends PagerFragment {

    private FastScrollRecyclerView materieRecyclerList;
    private TextView mediaTotaleMaterie;

    protected int mPeriod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_materia_first_period, container, false);

        materieRecyclerList = (FastScrollRecyclerView) rootView.findViewById(R.id.materieList);
        mediaTotaleMaterie = (TextView) rootView.findViewById(R.id.mediaTotaleMaterie);

        materieRecyclerList.setHasFixedSize(true);
        materieRecyclerList.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        init();
        setScrollListener();
        populateList();

        return rootView;
    }

    public void populateList(){

        ArrayList<Materia> mList = Subjects.getSubjects(getContext(), mPeriod);

        MaterieAdapter adapter = new MaterieAdapter(mList);
        materieRecyclerList.setAdapter(adapter);

        double defSum = 0;
        int NaNNumbers = 0;
        for (Materia item : mList){
            if(item.mediaMateria>0) {
                defSum += item.mediaMateria;
            }else NaNNumbers ++;
        }
        mediaTotaleMaterie.setText("Media totale: " + MathUtils.rintRound( defSum / ( mList.size() - NaNNumbers ), 1));
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

    public abstract void init();

}
