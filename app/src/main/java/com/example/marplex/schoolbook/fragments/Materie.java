package com.example.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        setToolbarLayout(pager);

        return rootView;
    }

    @Override
    public String getTitle() {
        return "Materie";
    }



}
