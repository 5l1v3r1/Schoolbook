package com.example.marplex.schoolbook.fragments.tabs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.marplex.schoolbook.Globals;
import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.SimpleSectionedRecyclerViewAdapter;
import com.example.marplex.schoolbook.adapters.votiAdapter;
import com.example.marplex.schoolbook.connections.ClassevivaAPI;
import com.example.marplex.schoolbook.interfaces.ClassevivaVoti;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondPeriod extends Fragment implements classeViva, ClassevivaVoti {

    ClassevivaAPI api;
    View rootView;
    RecyclerView list;
    SwipeRefreshLayout swipe;
    LayoutInflater inflater;

    public SecondPeriod() { }

    public static SecondPeriod newInstance() {
        return new SecondPeriod();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_second_period, container, false);

        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        refreshContent();
        swipe.setColorSchemeColors(R.color.red, R.color.yellow, R.color.green, R.color.blue);

        if(SharedPreferences.loadString(getActivity(), "datas", "voti")==null){
            api = new ClassevivaAPI(this, Globals.getInstance().getSession());
            api.getVotes(this, getActivity());
        }else{
            Type type = new TypeToken<List<Voto>>(){}.getType();
            List<Voto> datas = new Gson().fromJson(SharedPreferences.loadString(getActivity(), "datas", "voti"), type);

            List<Voto> voti = new ArrayList<>();
            for(int i=0; i<datas.size();i++){
                if(datas.get(i).periodo==2) voti.add(datas.get(i));
                else continue;
            }

            populateRecyclerView(voti);
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }});

        return rootView;
    }

    void refreshContent(){
        if(api!=null) api.getVotes(this, getActivity());
        else {
            api = new ClassevivaAPI(this, Globals.getInstance().getSession());
            api.getVotes(this, getActivity());
        }
    }

    @Override
    public void onVotiReceive(List<Voto> voto) {
        //Save in ROM
        SharedPreferences.saveString(getActivity(),"datas","voti",new Gson().toJson(voto));
        swipe.setRefreshing(false);

        List<Voto> voti = new ArrayList<>();
        for(int i=0; i<voto.size();i++){
            if(voto.get(i).periodo==2) voti.add(voto.get(i));
            else continue;
        }

        populateRecyclerView(voti);

    }

    void populateRecyclerView(List<Voto> voto){
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list = (RecyclerView) rootView.findViewById(R.id.listaVoti);
        list.setHasFixedSize(true);
        list.setLayoutManager(llm);
        votiAdapter adapter = new votiAdapter(voto);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
        if(voto.size()!=0){
        if(voto.get(0).materia.equals(voto.get(voto.size()-1).materia)){
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, voto.get(0).materia));
        }else{
            for(int i=0; i<voto.size(); i++) {
                if(i==0){
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, voto.get(i).materia));
                }else{
                    if(voto.get(i).materia!=voto.get(i-1).materia) {
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(i, voto.get(i).materia));
                    }
                }

            }
        }}

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(getActivity(),R.layout.section,R.id.section_text,adapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        list.setAdapter(mSectionedAdapter);
    }

    @Override
    public void onPageLoaded(String html) {

    }

    public void ordina(){
        View view = inflater.inflate(R.layout.ordina, null);
        final Dialog mBottomSheetDialog = new Dialog (getActivity(),R.style.MaterialDialogSheet);
        ListView Listamaterie = (ListView)view.findViewById( R.id.materie);

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        final ArrayList<String> materie = new Gson().fromJson(SharedPreferences.loadString(getActivity(), "materie", "materie"), type);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, materie.toArray(new String[materie.size()]));

        Listamaterie.setAdapter(arrayAdapter);
        Listamaterie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Type type = new TypeToken<List<Voto>>(){}.getType();
                List<Voto> datas = new Gson().fromJson(SharedPreferences.loadString(getActivity(), "datas", "voti"), type);
                List<Voto> tmp = new ArrayList<>();
                for(int i=0; i<datas.size();i++){
                    if(datas.get(i).periodo==2) tmp.add(datas.get(i));
                    else continue;
                }

                List<Voto> voti = new ArrayList<>();
                for(int x = 0; x< tmp.size(); x++){
                    String materia = materie.get(position);
                    if(tmp.get(x).materia.equals(materia)){
                        System.out.println(tmp.get(x).materia);
                        System.out.println(materia+" !");
                        System.out.println("OK");
                        voti.add(tmp.get(x));
                    }
                    else continue;
                }

                mBottomSheetDialog.hide();
                populateRecyclerView(voti);
            }
        });

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

    }

}