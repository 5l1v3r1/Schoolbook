package com.example.marplex.schoolbook.fragments.custom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.materieAdapter;
import com.example.marplex.schoolbook.models.Materia;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.example.marplex.schoolbook.utilities.Votes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MateriaFragment extends PagerFragment {

    private RecyclerView materieRecyclerList;
    private TextView mediaTotaleMaterie;

    protected int mPeriod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_materia_first_period, container, false);

        materieRecyclerList = (RecyclerView) rootView.findViewById(R.id.materieList);
        mediaTotaleMaterie = (TextView) rootView.findViewById(R.id.mediaTotaleMaterie);

        materieRecyclerList.setHasFixedSize(true);
        materieRecyclerList.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        init();
        populateList();

        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            boolean hideView = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (hideView) {
                    Runnable listener = new Runnable() {
                        @Override
                        public void run() {
                            mediaTotaleMaterie.setVisibility(View.GONE);
                        }
                    };
                    mediaTotaleMaterie.animate().translationY(-mediaTotaleMaterie.getBottom()).setInterpolator(new AccelerateInterpolator()).withEndAction(listener).start();
                } else {
                    Runnable listener = new Runnable() {
                        @Override
                        public void run() {
                            mediaTotaleMaterie.setVisibility(View.VISIBLE);
                        }
                    };
                    mediaTotaleMaterie.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).withEndAction(listener).start();
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

        return rootView;
    }

    public void populateList(){
        ArrayList<Materia> materieList = new ArrayList<>();

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        final ArrayList<String> materie = new Gson().fromJson(SharedPreferences.loadString(getActivity(), "materie", "materie"), type);

        for(String materia : materie){
            ArrayList<Voto> materiaVoti = Votes.getNumericalVotesByMateria(getActivity(), materia, mPeriod);

            double sum = 0;

            for(Voto voto : materiaVoti) {
                sum += getVoto(voto.voto);
            }

            double totalAverage = arrotondaRint(sum/materiaVoti.size(), 1);
            Materia mMateria = new Materia(totalAverage, materia);
            materieList.add(mMateria);
        }

        materieAdapter adapter = new materieAdapter(materieList);
        materieRecyclerList.setAdapter(adapter);

        double defSum = 0;
        int NaNNumbers = 0;
        for (Materia item : materieList){
            if(item.mediaMateria>0) {
                defSum += item.mediaMateria;
            }else NaNNumbers ++;
        }
        mediaTotaleMaterie.setText("Media totale: " + arrotondaRint( defSum / ( materieList.size() - NaNNumbers ), 1));
    }

    public abstract void init();

    public static double arrotondaRint(double value, int numCifreDecimali) {
        double temp = Math.pow(10, numCifreDecimali);
        return Math.rint(value * temp) / temp;
    }

    double getVoto(String val){
        if(val.length()==1){
            if(val.contains("+")) return 0;
            else if(val.contains("-"))return 0;
            else return Double.parseDouble(val);
        }else if(val.length()==2){
            if(val.endsWith("½")) {
                String value = val.substring(0, val.length()-1);
                return Double.parseDouble(value) + 0.5;
            }else if(val.endsWith("-")){
                String value = val.substring(0, val.length()-1);
                return Double.parseDouble(value) - 0.15;
            }else if(val.endsWith("+")){
                String value = val.substring(0, val.length()-1);
                return Double.parseDouble(value) + 0.15;
            }else if(val.equals("10")){
                return 10;
            }
        }else if(val.length()==3){
            if(val.endsWith("-")){
                String value = val.substring(0, val.length()-1);
                return Double.parseDouble(value) - 0.15;
            }else return 0;
        }
        return 0;
    }
}
