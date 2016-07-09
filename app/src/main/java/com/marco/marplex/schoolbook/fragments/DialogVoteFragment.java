package com.marco.marplex.schoolbook.fragments;

import com.marco.marplex.schoolbook.fragments.custom.VoteFragment;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

/**
 * Created by marco on 09/07/16.
 */

public class DialogVoteFragment extends VoteFragment {

    @Override public void init() {
        //Find the current period
        if(Votes.getVotesByPeriod(getContext(), 2) == null || Votes.getVotesByPeriod(getContext(), 2).size() == 0){
            mPeriod = 1;
        }else mPeriod = 2;
        mSwipe.setEnabled(false);
    }
    @Override public void ordina() {  }
    @Override public void eliminaOrdine() { }
    @Override public void onResponse(ArrayList<Voto> list) { mSwipe.setRefreshing(false); }
    @Override public ArrayList<Voto> getData(){
        int grade = getArguments().getInt("vote", 8);

        return Votes.getVotesByGrade(getContext(), grade);
    }
}