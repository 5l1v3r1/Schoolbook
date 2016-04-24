package com.example.marplex.schoolbook.fragments.custom;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.marplex.schoolbook.DashboardActivity;
import com.example.marplex.schoolbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class DrawerFragment extends Fragment {

    public abstract String getTitle();

    protected void setTabVisible(){
        ((DashboardActivity) getActivity()).tabLayout.setVisibility(View.VISIBLE);
    }

    protected void setTabGone(){
        ((DashboardActivity) getActivity()).tabLayout.setVisibility(View.GONE);
    }

    /**
     * setTabViewPager method
     *
     * @param pager Tab related view pager
     */
    protected void setTabViewPager(ViewPager pager){

    }

    /**
     * setMenu method
     *
     * @param menu Menu resource ID
     * @param listener Menu item click listener
     */
    protected void setMenu(int menu, Toolbar.OnMenuItemClickListener listener){

    }

    /**
     * setToolbarLayout method
     *
     * @param pager Tab related view pager
     * @param menu Menu resource ID
     * @param listener Menu item click listener
     */
    protected void setToolbarLayout(ViewPager pager, int menu, Toolbar.OnMenuItemClickListener listener){
        ((DashboardActivity) getActivity()).tabLayout.setVisibility(View.VISIBLE);
        ((DashboardActivity) getActivity()).tabLayout.setupWithViewPager(pager);
        ((DashboardActivity) getActivity()).setMenu(menu, listener);
    }

    protected void removeMenuItems(){
        //Blank menu
        ((DashboardActivity) getActivity()).setMenu(R.menu.menu_login, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    /**
     * setToolbarLayout method
     *
     * @param pager Tab related view pager
     */
    protected void setToolbarLayout(ViewPager pager){
        ((DashboardActivity) getActivity()).tabLayout.setVisibility(View.VISIBLE);
        ((DashboardActivity) getActivity()).tabLayout.setupWithViewPager(pager);

        //Blank menu
        ((DashboardActivity) getActivity()).setMenu(R.menu.menu_login, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }
}
