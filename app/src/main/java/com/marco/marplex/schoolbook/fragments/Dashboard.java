package com.marco.marplex.schoolbook.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.SettingsActivity;
import com.marco.marplex.schoolbook.adapters.SectionPagerAdapter;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.fragments.tabs.Home;
import com.marco.marplex.schoolbook.fragments.tabs.Reminds;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends DrawerFragment {

    @Bind(R.id.pager) ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);

        /**
         * @see SectionPagerAdapter
         */
        pager.setAdapter(new SectionPagerAdapter(this.getChildFragmentManager(), new Home(), new Reminds()));

        /**
         * @see DrawerFragment
         */
        setToolbarLayout(pager, R.menu.menu_dashboard, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.refresh:
                        ClassevivaCaller caller = new ClassevivaCaller(new ClassevivaCallback() {
                            @Override
                            public void onResponse(final ArrayList list) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Votes.saveVotes(getActivity(), list);
                                        Toast.makeText(getActivity(), "Finito", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }, getActivity());
                        caller.getVotes();
                        Toast.makeText(getActivity(), "Sto aggiornando il tuo registro...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(getActivity(), SettingsActivity.class));
                        break;
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public String getTitle() {
        return "Dashboard";
    }

}
