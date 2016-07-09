package com.marco.marplex.schoolbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.marco.marplex.schoolbook.fragments.DialogVoteFragment;
import com.marco.marplex.schoolbook.utilities.Votes;

public class VoteDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_dialog);

        Intent intent = getIntent();
        DialogVoteFragment fragment = new DialogVoteFragment();
        Bundle b = new Bundle();
        b.putInt("vote", intent.getIntExtra("vote", 8));
        fragment.setArguments(b);

        setTitle(Votes.getVotesByGrade(this, intent.getIntExtra("vote", 8)).size() + " voti");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }
}


