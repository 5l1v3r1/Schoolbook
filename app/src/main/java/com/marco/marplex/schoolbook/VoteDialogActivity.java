package com.marco.marplex.schoolbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.marco.marplex.schoolbook.fragments.custom.VoteFragment;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

public class VoteDialogActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_dialog);

        intent = getIntent();

        setTitle(Votes.getVotesByGrade(this, intent.getIntExtra("vote", 8)).size() + " voti");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, new VoteFragment() {
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
                return Votes.getVotesByGrade(getContext(), intent.getIntExtra("vote", 8));
            }
        });
        transaction.commit();
    }
}
