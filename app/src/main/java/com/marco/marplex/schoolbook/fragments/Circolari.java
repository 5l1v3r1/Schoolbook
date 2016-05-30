package com.marco.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.adapters.ComunicationAdapter;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.models.Comunication;
import com.marco.marplex.schoolbook.utilities.Comunications;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Circolari extends DrawerFragment implements ClassevivaCallback<Comunication>{

    @Bind(R.id.rv_circolari) RecyclerView mList;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipe;

    private ClassevivaCaller mCaller;
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_circolari, container, false);
        ButterKnife.bind(this, mRootView);

        mCaller = new ClassevivaCaller(this, getContext());

        mSwipe.setColorSchemeResources(
                R.color.colorPrimaryRed,
                R.color.colorPrimaryDarkRed
        );

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCaller.getSchoolComunication();
            }
        });

        setTabGone();
        removeMenuItems();

        if(Comunications.isComunicationsSaved(getContext())){
            populateRecyclerView(
                    Comunications.getSavedComunications(getContext())
            );
        }else mCaller.getSchoolComunication();

        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);

        return mRootView;
    }

    private void populateRecyclerView(ArrayList<Comunication> list){
        mList.setAdapter(new ComunicationAdapter(list, getContext()));
    }

    @Override
    public String getTitle() {
        return "Circolari";
    }

    @Override
    public void onResponse(final ArrayList<Comunication> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                populateRecyclerView(list);
                Comunications.saveComunications(getContext(), list);
                mSwipe.setRefreshing(false);
            }
        });
    }
}
