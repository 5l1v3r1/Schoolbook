package com.marco.marplex.schoolbook.fragments.tabs;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.marco.marplex.schoolbook.fragments.custom.VoteFragment;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondPeriod extends VoteFragment {

    private Dialog mDialog = null;

    @Override
    public void init() {
        mPeriod = 2;
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