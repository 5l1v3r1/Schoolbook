package com.example.marplex.schoolbook.fragments.custom;

/**
 * Created by marco on 2/16/16.
 */

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.CaldroidSampleCustomAdapter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class CustomCaldroid extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CaldroidSampleCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

    @Override
    protected int getGridViewRes() {
        return R.layout.custom_grid;
    }

}
