package com.marco.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.adapters.ArgumentsAdapter;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.models.Argument;
import com.marco.marplex.schoolbook.utilities.Subjects;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link DrawerFragment} subclass.
 */
public class ArgumentsFragment extends DrawerFragment {

    private View rootView;
    @Bind(R.id.argumentList) RecyclerView mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_arguments, container, false);
        ButterKnife.bind(this, rootView);

        setTabGone();
        removeMenuItems();

        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);

        List<String> subjects = Subjects.getSubjects(getContext());
        ArrayList<Argument> argumentList = new ArrayList();
        if(subjects != null) {
            for (String subject : subjects) {
                int icon;

                //It can be improved a lot
                if (subject.equals("Chimica")) icon = R.drawable.flask;
                else if (subject.equals("Fisica")) icon = R.drawable.magnet;
                else if (subject.equals("Scienze")) icon = R.drawable.science;
                else if (subject.equals("Diritto")) icon = R.drawable.justice;
                else if (subject.equals("Italiano")) icon = R.drawable.books;
                else if (subject.equals("Inglese")) icon = R.drawable.big_ben;
                else if (subject.equals("Matematica")) icon = R.drawable.calculator;
                else if (subject.equals("Religione")) icon = R.drawable.bible;
                else if (subject.equals("Scienze Applicate")) icon = R.drawable.microscope;
                else if (subject.equals("Ginnastica")) icon = R.drawable.strength;
                else if (subject.equals("Storia")) icon = R.drawable.history;
                else if (subject.equals("Tecnica")) icon = R.drawable.compass;
                else if (subject.equals("Informatica")) icon = R.drawable.laptop;
                else icon = R.drawable.notebook;

                Argument argument = new Argument(icon, subject);
                argumentList.add(argument);
            }
        }

        if(subjects.size() != 0) rootView.findViewById(R.id.nothingHere).setVisibility(View.GONE);
        mList.setAdapter(new ArgumentsAdapter(argumentList, getContext(), getActivity()));

        return rootView;
    }

    @Override
    public String getTitle() {
        return "Argomenti";
    }
}
