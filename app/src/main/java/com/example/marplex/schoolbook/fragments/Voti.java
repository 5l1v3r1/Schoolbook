package com.example.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.fragments.tabs.FirstPeriod;
import com.example.marplex.schoolbook.fragments.tabs.Reminds;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Voti extends Fragment {

    private ViewPager pager;
    public Voti() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voti, container, false);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        pager = (ViewPager)rootView.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        pager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("1° PERIODO"));
        tabLayout.addTab(tabLayout.newTab().setText("2° PERIODO"));
        tabLayout.setupWithViewPager(pager);

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SparseArray<android.support.v4.app.Fragment> registeredFragments = new SparseArray<android.support.v4.app.Fragment>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment fg = null;
            if(position==0) fg = new FirstPeriod();
            else if(position==1) fg = new Reminds();
            return fg;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "1° PERIODO";
                case 1:
                    return "2° PERIODO";
            }
            return null;
        }

        public android.support.v4.app.Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

}
