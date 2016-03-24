package com.example.marplex.schoolbook.fragments.tabs;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.example.marplex.schoolbook.fragments.custom.VoteFragment;
import com.example.marplex.schoolbook.interfaces.ClassevivaVoti;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondPeriod extends VoteFragment implements ClassevivaVoti {

    private Dialog mDialog = null;

    @Override
    public void init() {
        mPeriod = 2;
        setVotiListener(this);
    }

    @Override
    public void onVotiReceive(ArrayList<Voto> voto) {
        if(voto==null) refreshContent();

        /**
         * @see Votes
         */
        Votes.saveVotes(getActivity(), voto);
        mSwipe.setRefreshing(false);

        populateRecyclerView(Votes.getVotesByPeriod(voto, mPeriod));
    }

    @Override
    public void ordina(){
        mDialog = viewOrderDialog(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Voto> voti = Votes.getVotesByMateria(getActivity(), materie.get(position), mPeriod);
                populateRecyclerView(voti);
                mDialog.hide();
            }
        });
    }

    @Override
    public void eliminaOrdine(){
        populateRecyclerView(Votes.getVotesByPeriod(getActivity(), mPeriod));
    }

    @Override
    public String getPageTitle() {
        return "2° Periodo";
    }
}