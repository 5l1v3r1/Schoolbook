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

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.adapters.SimpleSectionedRecyclerViewAdapter;
import com.marco.marplex.schoolbook.adapters.VotiAdapter;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.interfaces.ClassevivaVoti;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;
import com.marco.marplex.schoolbook.utilities.Votes;
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
public abstract class VoteFragment extends PagerFragment implements ClassevivaCallback<Voto> {

    private View mRootView;
    private LinearLayoutManager mLlm;
    private ClassevivaCaller mApi;

    private LayoutInflater mInflater;

    protected int mPeriod;
    protected ArrayList<String> materie;

    @Bind(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mSwipe;
    @Bind(R.id.listaVoti) RecyclerView mList;

    private AppBarLayout abl;

    private ClassevivaVoti mVotesListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_first_period, container, false);
        ButterKnife.bind(this, mRootView);

        abl = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
        mInflater = inflater;

        mLlm = new LinearLayoutManager(getActivity());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(mLlm);
        mSwipe.setColorSchemeColors(R.color.green);

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
            final ArrayList<Voto> voti = Votes.getVotesByPeriod(getActivity(), mPeriod);
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

    //Abstract methods
    public abstract void init();
    public abstract void ordina();
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
                SimpleSectionedRecyclerViewAdapter(getActivity(),R.layout.section,R.id.section_text,adapter, mPeriod, abl, getActivity());
        mSectionedAdapter.setSections(sections.toArray(dummy));

        mList.setAdapter(mSectionedAdapter);
        mSwipe.setRefreshing(false);
    }

    protected Dialog viewOrderDialog(AdapterView.OnItemClickListener clickListener){
        View view = mInflater.inflate(R.layout.ordina, null);
        final Dialog mBottomSheetDialog = new Dialog (getActivity(),R.style.MaterialDialogSheet);
        ListView Listamaterie = (ListView)view.findViewById( R.id.materie);

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        materie = new Gson().fromJson(SharedPreferences.loadString(getActivity(), "materie", "materie"), type);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, materie.toArray(new String[materie.size()]));

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
    public String getPageTitle() {
        return null;
    }

    public boolean isDataSaved(){
        return !( SharedPreferences.loadString(getActivity(), "datas", "voti") == null );
    }

}