package com.marco.marplex.schoolbook.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.marco.marplex.schoolbook.fragments.custom.PagerFragment;

/**
 * Created by marco on 3/23/16.
 */
public class SectionPagerAdapter extends FragmentPagerAdapter {
    SparseArray<PagerFragment> registeredFragments = new SparseArray<PagerFragment>();
    private PagerFragment[] pagerFragments;

    public SectionPagerAdapter(FragmentManager fm, PagerFragment... pagerFragments) {
        super(fm);
        this.pagerFragments = pagerFragments;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        for(int i = 0; i < pagerFragments.length; i++) {
            if(position == i) return pagerFragments[i];
        }
        return null;
    }

    @Override
    public int getCount() {
        return pagerFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        for(int i = 0; i < pagerFragments.length; i++) {
            if(position == i) return pagerFragments[i].getPageTitle();
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PagerFragment fragment = (PagerFragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public PagerFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
