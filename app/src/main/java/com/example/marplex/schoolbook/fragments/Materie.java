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
import com.example.marplex.schoolbook.fragments.tabs.MateriaFirstPeriod;
import com.example.marplex.schoolbook.fragments.tabs.MateriaSecondPeriod;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Materie extends Fragment {

    @Bind(R.id.pager) ViewPager pager;

    public Materie() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_materie, container, false);
        ButterKnife.bind(this, rootView);

        pager.setAdapter(new SectionsPagerAdapter(this.getChildFragmentManager()));

        ((DashboardActivity) getActivity()).tabLayout.setVisibility(View.VISIBLE);
        ((DashboardActivity) getActivity()).tabLayout.setupWithViewPager(pager);
        ((DashboardActivity) getActivity()).setMenu(R.menu.menu_login, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
                case 0: return MateriaFirstPeriod.newInstance();
                case 1: return MateriaSecondPeriod.newInstance();
                default: return MateriaFirstPeriod.newInstance();
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
