package com.marco.marplex.schoolbook.fragments.custom;


import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
abstract public class PagerFragment extends Fragment {
    public abstract String getPageTitle();

    public void eliminaOrdine(){};
    public void ordina(){};
    public void showChart(){};

}
