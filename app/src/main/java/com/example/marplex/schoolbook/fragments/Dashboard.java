package com.example.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.sliding_tabs);

        pager.setAdapter(new SectionsPagerAdapter(this.getChildFragmentManager()));
        Toast.makeText(getActivity(), "Dashboard", Toast.LENGTH_SHORT).show();

        tabLayout.setupWithViewPager(pager);
        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Toast.makeText(getActivity(), "POSITION", Toast.LENGTH_SHORT).show();
            switch (position){
                case 0:
                    Toast.makeText(getActivity(), "Home", Toast.LENGTH_SHORT).show();
                    return Home.newInstance();
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
