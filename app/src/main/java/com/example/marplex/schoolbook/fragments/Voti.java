package com.example.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.DashboardActivity;
import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.fragments.tabs.FirstPeriod;
import com.example.marplex.schoolbook.fragments.tabs.SecondPeriod;


/**
 * A simple {@link Fragment} subclass.
 */
public class Voti extends Fragment {

    private ViewPager pager;
    public Voti() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroy();
        onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voti, container, false);

        pager = (ViewPager)rootView.findViewById(R.id.viewpager);
        pager.setAdapter(new SectionsPagerAdapter(this.getChildFragmentManager()));

        ( (DashboardActivity) getActivity() ).tabLayout.setVisibility(View.VISIBLE);
        ( (DashboardActivity) getActivity() ).tabLayout.setupWithViewPager(pager);

        DashboardActivity main = (DashboardActivity) getActivity();
        main.setMenu(R.menu.voti, new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.ordina) {
                    if(pager.getCurrentItem()==0){
                        FirstPeriod firstPeriod = (FirstPeriod) ((SectionsPagerAdapter)pager.getAdapter()).getRegisteredFragment(pager.getCurrentItem());
                        firstPeriod.ordina();
                    }else{
                        SecondPeriod secondPeriod = (SecondPeriod) ((SectionsPagerAdapter)pager.getAdapter()).getRegisteredFragment(pager.getCurrentItem());
                        secondPeriod.ordina();
                    }

                }
                return false;
            }
        });

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0: return FirstPeriod.newInstance();
                case 1: return SecondPeriod.newInstance();
                default: return FirstPeriod.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "1° Periodo";
                case 1:
                    return "2° Periodo";
                default: return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

    }

}
