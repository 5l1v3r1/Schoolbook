package com.marco.marplex.schoolbook.fragments.tabs;


import android.support.v4.app.Fragment;

import com.marco.marplex.schoolbook.fragments.custom.MateriaFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MateriaSecondPeriod extends MateriaFragment {

    @Override
    public String getPageTitle() {
        return "2° Periodo";
    }

    @Override
    public void init() {
        mPeriod = 2;
    }

}
