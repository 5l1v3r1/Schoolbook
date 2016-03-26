package com.example.marplex.schoolbook.fragments.custom;


import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
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

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.SimpleSectionedRecyclerViewAdapter;
import com.example.marplex.schoolbook.adapters.votiAdapter;
import com.example.marplex.schoolbook.connections.ClassevivaAPI;
import com.example.marplex.schoolbook.interfaces.ClassevivaVoti;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.Credentials;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.example.marplex.schoolbook.utilities.Votes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class VoteFragment extends PagerFragment implements classeViva{

    private View mRootView;
    private LinearLayoutManager mLlm;
    private ClassevivaAPI mApi;

    private LayoutInflater mInflater;

    protected int mPeriod;
    protected ArrayList<String> materie;
    protected SwipeRefreshLayout mSwipe;

    private ClassevivaVoti mVotesListener;

    private RecyclerView mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_first_period, container, false);
        mInflater = inflater;

        mLlm = new LinearLayoutManager(getActivity());

        mSwipe = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipe.setColorSchemeColors(R.color.green);

        init();

        if(!isDataSaved()){
            mApi = new ClassevivaAPI(this);
            mApi.getVotes(mVotesListener, getActivity());
        }else{

        /**
         * @see Votes
         */
        ArrayList<Voto> voti = Votes.getVotesByPeriod(getActivity(), mPeriod);
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

    @Override
    public void onPageLoaded(String html) { }

    protected void refreshContent(){
        if(mApi!=null) mApi.getVotes(mVotesListener, getActivity());
        else {
            mApi = new ClassevivaAPI(this);
            mApi.setSession(Credentials.getSession(getActivity()));
            mApi.getVotes(mVotesListener, getActivity());
        }
    }

    protected void populateRecyclerView(List<Voto> voto){

        mList = (RecyclerView) mRootView.findViewById(R.id.listaVoti);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(mLlm);
        votiAdapter adapter = new votiAdapter(voto);

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
            }}

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(getActivity(),R.layout.section,R.id.section_text,adapter, mPeriod);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        mList.setAdapter(mSectionedAdapter);
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
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        Listamaterie.setOnItemClickListener(clickListener);

        return mBottomSheetDialog;
    }

    protected void setVotiListener(ClassevivaVoti mVotesListener) {
        this.mVotesListener = mVotesListener;
    }

    @Override
    public String getPageTitle() {
        return null;
    }

    public boolean isDataSaved(){
        return !( SharedPreferences.loadString(getActivity(), "datas", "voti") == null );
    }

}