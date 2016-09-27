package com.marco.marplex.schoolbook.fragments.tabs;

import android.support.v4.app.Fragment;

import com.marco.marplex.schoolbook.fragments.custom.VoteFragment;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstPeriod extends VoteFragment {

    @Override
    public void init(){
        mPeriod = 1;
    }

    @Override
    public void eliminaOrdine(){
        populateRecyclerView(Votes.getVotesByPeriod(getActivity(), mPeriod));
    }

    @Override
    public String getPageTitle() {
        return "1° Periodo";
    }

    @Override
    public void onResponse(final ArrayList<Voto> list) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (list == null || list.size() == 0) return;

                        Votes.saveVotes(getActivity(), list);
                        mSwipe.setRefreshing(false);

                        hideEmptyState();
                        populateRecyclerView(Votes.getVotesByPeriod(list, mPeriod));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
