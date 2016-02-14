package com.example.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.DashboardActivity;
import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.fragments.tabs.Home;
import com.example.marplex.schoolbook.fragments.tabs.Reminds;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private ViewPager pager;
    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        DashboardActivity main = (DashboardActivity) getActivity();
        main.setMenu(R.menu.menu_login, new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setAdapter(new SectionsPagerAdapter(this.getChildFragmentManager()));

        ( (DashboardActivity) getActivity() ).tabLayout.setupWithViewPager(pager);

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return Home.newInstance();
                case 1: return Reminds.newInstance();
                default: return Home.newInstance();
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
                    return "Home";
                case 1:
                    return "Reminds";
                default: return null;
            }
        }
    }
}
