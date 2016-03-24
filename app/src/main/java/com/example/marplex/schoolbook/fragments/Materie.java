package com.example.marplex.schoolbook.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.CompareActivity;
import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.SectionPagerAdapter;
import com.example.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.example.marplex.schoolbook.fragments.tabs.MateriaFirstPeriod;
import com.example.marplex.schoolbook.fragments.tabs.MateriaSecondPeriod;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Materie extends DrawerFragment {

    @Bind(R.id.pager) ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_materie, container, false);
        ButterKnife.bind(this, rootView);

        /**
         * @see SectionPagerAdapter
         */
        pager.setAdapter(new SectionPagerAdapter(this.getChildFragmentManager(),
                new MateriaFirstPeriod(),
                new MateriaSecondPeriod()));

        /**
         * @see DrawerFragment
         */
        setToolbarLayout(pager, R.menu.materie, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.compara:
                        startActivity(new Intent(getActivity(), CompareActivity.class));
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public String getTitle() {
        return "Materie";
    }



}
