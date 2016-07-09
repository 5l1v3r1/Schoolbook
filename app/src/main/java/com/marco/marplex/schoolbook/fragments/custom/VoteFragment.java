package com.marco.marplex.schoolbook.fragments.custom;


import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.adapters.SimpleSectionedRecyclerViewAdapter;
import com.marco.marplex.schoolbook.adapters.VotiAdapter;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class VoteFragment extends PagerFragment implements ClassevivaCallback<Voto> {

    private View mRootView;
    private LinearLayoutManager mLlm;
    private ClassevivaCaller mApi;
    private  Dialog subjectDialog, majorThanDialog, equalsToDialog, mDialog;

    private LayoutInflater mInflater;

    protected int mPeriod;
    protected ArrayList<String> materie;

    @Bind(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mSwipe;
    @Bind(R.id.listaVoti) RecyclerView mList;

    private AppBarLayout abl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_first_period, container, false);
        ButterKnife.bind(this, mRootView);

        abl = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
        mInflater = inflater;

        mLlm = new LinearLayoutManager(getContext());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(mLlm);
        mSwipe.setColorSchemeResources(
                R.color.colorPrimaryGreen,
                R.color.colorPrimaryDarkGreen
        );

        init();

        if(!isDataSaved()){
            mApi = new ClassevivaCaller(this, getContext());
            mApi.getVotes();
            mSwipe.post(new Runnable() {
                @Override
                public void run() {
                    mSwipe.setRefreshing(true);
                }
            });
        }else{
            final ArrayList<Voto> voti = getData();
            populateRecyclerView(voti);
        }

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        return mRootView;
    }



    public ArrayList<Voto> getData(){
        return Votes.getVotesByPeriod(getContext(), mPeriod);
    }

    //Abstract methods
    public abstract void init();
    public abstract void eliminaOrdine();

    protected void refreshContent(){
        if(mApi!=null) mApi.getVotes();
        else {
            mApi = new ClassevivaCaller(this, getContext());
            mApi.getVotes();
        }
    }

    protected void populateRecyclerView(List<Voto> voto){

        VotiAdapter adapter = new VotiAdapter(voto);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        if(voto.size() != 0){
            if(voto.get(0).materia.equals(voto.get(voto.size()-1).materia)){
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, voto.get(0).materia));
            }else{
                for(int i=0; i<voto.size(); i++) {
                    if(i==0){
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, voto.get(i).materia));
                    }else{
                        if( !(voto.get(i).materia.endsWith(voto.get(i-1).materia)) ) {
                            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(i, voto.get(i).materia));
                        }
                    }

                }
            }
        }

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(getContext(),R.layout.section,R.id.section_text,adapter, mPeriod, abl, getActivity());
        mSectionedAdapter.setSections(sections.toArray(dummy));

        mList.setAdapter(mSectionedAdapter);
        mSwipe.setRefreshing(false);
    }

    protected Dialog viewOrderDialog(AdapterView.OnItemClickListener clickListener){
        View view = mInflater.inflate(R.layout.ordina, null);
        final Dialog mBottomSheetDialog = new Dialog (getContext(),R.style.MaterialDialogSheet);
        ListView Listamaterie = (ListView)view.findViewById( R.id.materie);

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        materie = new Gson().fromJson(SharedPreferences.loadString(getContext(), "materie", "materie"), type);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, materie.toArray(new String[materie.size()]));

        Listamaterie.setAdapter(arrayAdapter);

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        Listamaterie.setOnItemClickListener(clickListener);

        return mBottomSheetDialog;
    }

    @Override
    public void ordina(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ordina, null, false);
        mDialog = new Dialog (getContext(), R.style.MaterialDialogSheet);

        setLayoutForDialog(mDialog, view);
        String[] choices = new String[]{"Materia uguale a..", "Voto maggiore di..", "Voto uguale a.."};
        ListView listView = (ListView)view.findViewById( R.id.materie);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, choices);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDialog.cancel();
                switch (i){
                    case 0:
                        subjectDialog = viewOrderDialog(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ArrayList<Voto> voti = Votes.getVotesByMateria(getActivity(), materie.get(position), mPeriod);
                                populateRecyclerView(voti);
                                subjectDialog.hide();
                            }
                        });
                        break;
                    case 1:
                        View majorView = mInflater.inflate(R.layout.ordina, null);
                        majorThanDialog = new Dialog (getContext(), R.style.MaterialDialogSheet);

                        final String[] choices2 = new String[10];
                        for(int index = 1; index <= 10; index++) choices2[index-1] = index+"";

                        ListView listView2 = (ListView)majorView.findViewById( R.id.materie);
                        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, choices2);
                        listView2.setAdapter(arrayAdapter2);
                        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int than = Integer.parseInt(choices2[i]);
                                ArrayList<Voto> voti = Votes.getVotesByGreaterThan(getContext(), than, mPeriod);
                                populateRecyclerView(voti);
                                majorThanDialog.hide();
                            }
                        });

                        majorView.findViewById(R.id.dialog_title).setVisibility(View.GONE);
                        majorThanDialog.setContentView(majorView);
                        majorThanDialog.setCancelable(true);
                        majorThanDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        majorThanDialog.getWindow().setGravity(Gravity.BOTTOM);

                        majorThanDialog.show();
                        break;
                    case 2:
                        View equalsToView = mInflater.inflate(R.layout.ordina, null);
                        equalsToDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);

                        final String[] choices1 = new String[10];
                        for(int index = 1; index <= 10; index++) choices1[index-1] = index+"";
                        for(int i1 = 0; i1 < choices1.length; i1++) System.out.println("LOL "+choices1[i1]);

                        ListView listView1 = (ListView) equalsToView.findViewById( R.id.materie);
                        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, choices1);
                        listView1.setAdapter(arrayAdapter1);
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int than = Integer.parseInt(choices1[i]);
                                ArrayList<Voto> voti = Votes.getVotesByGrade(getContext(), than, mPeriod);
                                populateRecyclerView(voti);
                                equalsToDialog.hide();
                            }
                        });

                        equalsToView.findViewById(R.id.dialog_title).setVisibility(View.GONE);
                        equalsToDialog.setContentView(equalsToView);
                        equalsToDialog.setCancelable(true);
                        equalsToDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        equalsToDialog.getWindow().setGravity(Gravity.BOTTOM);

                        equalsToDialog.show();

                        break;
                }
            }
        });
    }

    private void setLayoutForDialog(Dialog dialog, View view){
        view.findViewById(R.id.dialog_title).setVisibility(View.VISIBLE);
        ((TextView)view.findViewById(R.id.dialog_title)).setText("Vedi solo valutazioni con");
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    @Override
    public String getPageTitle() {
        return null;
    }

    public boolean isDataSaved(){
        return !( SharedPreferences.loadString(getContext(), "datas", "voti") == null );
    }

}