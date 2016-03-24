package com.example.marplex.schoolbook.fragments.tabs;


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
import com.example.marplex.schoolbook.fragments.custom.PagerFragment;
import com.example.marplex.schoolbook.models.Materia;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MateriaFirstPeriod extends PagerFragment {

    @Bind(R.id.materieList) RecyclerView materieRecyclerList;
    @Bind(R.id.mediaTotaleMaterie) TextView mediaTotaleMaterie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_materia_first_period, container, false);
        ButterKnife.bind(this, rootView);

        materieRecyclerList.setHasFixedSize(true);
        materieRecyclerList.setLayoutManager(new GridLayoutManager(getActivity(), 2));

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
        Type type = new TypeToken<List<Voto>>(){}.getType();
        List<Voto> datas = new Gson().fromJson(SharedPreferences.loadString(getActivity(), "datas", "voti"), type);

        List<Voto> voti = new ArrayList<>();
        for(int i=0; i<datas.size();i++){
            if(datas.get(i).periodo==1) {
                voti.add(datas.get(i));
            }
            else continue;
        }datas.clear();

        Type type1 = new TypeToken<ArrayList<String>>(){}.getType();
        final ArrayList<String> materie = new Gson().fromJson(SharedPreferences.loadString(getActivity(), "materie", "materie"), type1);

        for(int i=0; i<materie.size(); i++){
            List<Voto> materiaVoti = new ArrayList<>();
            for(int x=0; x<voti.size();x++){
                if(voti.get(x).materia.equals(materie.get(i))){
                    if( !(voti.get(x).voto.equals("-") || voti.get(x).voto.equals("+") || voti.get(x).voto.equals("nav")) ){
                        materiaVoti.add(voti.get(x));
                    }
                }else continue;
            }
            double sum = 0;
            for(int z=0; z<materiaVoti.size(); z++){
                sum += getVoto(materiaVoti.get(z).voto);
            }

            double totalAverage = arrotondaRint(sum/materiaVoti.size(), 1);
            Materia materia = new Materia(totalAverage, materie.get(i));
            materieList.add(materia);
        }

        materieAdapter adapter = new materieAdapter(materieList);
        materieRecyclerList.setAdapter(adapter);

        double defSum = 0;
        int NaNNumbers = 0;
        for (Materia materia : materieList){
            if(!isNaN(materia.mediaMateria)) {
                defSum = defSum + materia.mediaMateria;
            }else NaNNumbers ++;
        }
        mediaTotaleMaterie.setText("Media totale: " + arrotondaRint( defSum / ( materieList.size() - NaNNumbers ), 1));


    }

    public static double arrotondaRint(double value, int numCifreDecimali) {
        double temp = Math.pow(10, numCifreDecimali);
        return Math.rint(value * temp) / temp;
    }

    boolean isNaN(double x){return x != x;}
    
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

    @Override
    public String getPageTitle() {
        return "1° Periodo";
    }
}
