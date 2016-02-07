package com.example.marplex.schoolbook.fragments.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.votiAdapter;
import com.example.marplex.schoolbook.connections.ClassevivaAPI;
import com.example.marplex.schoolbook.interfaces.ClassevivaVoti;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.google.gson.Gson;

import java.util.List;

public class Home extends Fragment implements classeViva, ClassevivaVoti{

    ClassevivaAPI api;
    View rootView;
    RecyclerView list;
    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home, container, false);
        return rootView;
    }

    public static Home newInstance() {
        return new Home();
    }

    @Override
    public void onVotiReceive(List<Voto> voto) {
        //Save in ROM
        SharedPreferences.saveString(getActivity(),"datas","voti",new Gson().toJson(voto));
        populateRecyclerView(voto);

    }

    void populateRecyclerView(List<Voto> voto){
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list = (RecyclerView) rootView.findViewById(R.id.listaNotifiche);
        list.setHasFixedSize(true);
        list.setLayoutManager(llm);
        votiAdapter adapter = new votiAdapter(voto);
        list.setAdapter(adapter);
    }

    @Override
    public void onPageLoaded(String html) {

    }
}
