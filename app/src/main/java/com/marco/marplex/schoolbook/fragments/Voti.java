package com.marco.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.adapters.SectionPagerAdapter;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.fragments.tabs.FirstPeriod;
import com.marco.marplex.schoolbook.fragments.tabs.SecondPeriod;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class Voti extends DrawerFragment {

    @Bind(R.id.viewpager) ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voti, container, false);
        ButterKnife.bind(this, rootView);

        /**
         * @see SectionPagerAdapter
         */
        pager.setAdapter(new SectionPagerAdapter(this.getChildFragmentManager(),
                new FirstPeriod(),
                new SecondPeriod()));

        /**
         * @see DrawerFragment
         */
        setToolbarLayout(pager, R.menu.voti, new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ordina:
                        ((SectionPagerAdapter) pager.getAdapter())
                                .getRegisteredFragment(pager.getCurrentItem()).ordina();
                    case R.id.elimina_filtro:
                        ((SectionPagerAdapter) pager.getAdapter())
                                .getRegisteredFragment(pager.getCurrentItem()).eliminaOrdine();
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public String getTitle() {
        return "Voti";
    }


}
