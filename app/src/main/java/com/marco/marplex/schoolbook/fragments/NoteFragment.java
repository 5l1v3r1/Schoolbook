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
import com.marco.marplex.schoolbook.adapters.NoteAdapter;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.models.Note;
import com.marco.marplex.schoolbook.utilities.Notes;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends DrawerFragment implements ClassevivaCallback<Note>{

    @Bind(R.id.rv_note) RecyclerView mList;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipe;

    private View rootView;


    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, rootView);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));

        final ClassevivaCaller caller = new ClassevivaCaller(this, getContext());

        mSwipe.setColorSchemeResources(
                R.color.colorPrimaryAmber,
                R.color.colorPrimaryDarkAmber
        );

        setTabGone();
        removeMenuItems();

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                caller.getNotes();
            }
        });

        if(Notes.isNotesSaved(getContext())){
            populateRecyclerView(Notes.getSavedNotes(getContext()));
        }else caller.getNotes();
        return rootView;
    }

    private void populateRecyclerView(ArrayList<Note> list){
        mList.setAdapter(new NoteAdapter(list, getContext()));
    }

    @Override
    public void onResponse(final ArrayList<Note> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipe.setRefreshing(false);
                populateRecyclerView(list);
                Notes.saveNotes(getContext(), list);
            }
        });
    }

    @Override
    public String getTitle() {
        return "Note";
    }
}
