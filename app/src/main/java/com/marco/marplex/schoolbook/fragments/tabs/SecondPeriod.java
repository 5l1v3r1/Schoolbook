package com.marco.marplex.schoolbook.fragments.tabs;

import android.support.v4.app.Fragment;

import com.marco.marplex.schoolbook.fragments.custom.VoteFragment;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondPeriod extends VoteFragment {

    @Override
    public void init() {
        mPeriod = 2;
    }

    @Override
    public void eliminaOrdine(){
        populateRecyclerView(Votes.getVotesByPeriod(getActivity(), mPeriod));
    }

    @Override
    public String getPageTitle() {
        return "2° Periodo";
    }

    @Override
    public void onResponse(final ArrayList<Voto> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(list==null) refreshContent();

                /**
                 * @see Votes
                 */
                Votes.saveVotes(getActivity(), list);
                mSwipe.setRefreshing(false);

                populateRecyclerView(Votes.getVotesByPeriod(list, mPeriod));
            }
        });
    }
}