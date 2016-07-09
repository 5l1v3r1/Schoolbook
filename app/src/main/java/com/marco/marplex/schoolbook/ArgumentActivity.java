package com.marco.marplex.schoolbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.marco.marplex.schoolbook.adapters.ArgumentsActivityAdapter;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.models.Argument;
import com.marco.marplex.schoolbook.utilities.Arguments;
import com.marco.marplex.schoolbook.utilities.Connection;
import com.marco.marplex.schoolbook.utilities.Subjects;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArgumentActivity extends AppCompatActivity {

    @Bind(R.id.img_model_argument) ImageView icon;
    @Bind(R.id.txt_model_argument) TextView subject;
    @Bind(R.id.card_model_argument) CardView card;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.lv_list) ListView mList;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argument);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle("Argomenti");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent i = getIntent();

        swipe.setColorSchemeResources(
                R.color.colorPrimaryPurple,
                R.color.colorPrimaryDarkPurple
        );

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
            }
        });

        icon.setImageResource(i.getIntExtra("icon", 0));
        card.setCardBackgroundColor(i.getIntExtra("color", 0));
        subject.setText(i.getStringExtra("argument"));
        subject.setTextColor(i.getIntExtra("textColor", 0));
        toolbar.setBackgroundColor(i.getIntExtra("color", 0));

        if(i.getIntExtra("color", 0) == 0x000000) subject.setTextColor(Color.WHITE);

        //Start retrieving data
        if(Connection.isNetworkAvailable(this)){
            ClassevivaCallback<Argument> callback = new ClassevivaCallback<Argument>() {
                @Override
                public void onResponse(final ArrayList<Argument> list) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Arguments.saveArguments(ArgumentActivity.this, list, i.getStringExtra("argument"));
                            populateList(list);
                            swipe.setRefreshing(false);
                        }
                    });
                }
            };
            ClassevivaCaller caller = new ClassevivaCaller(callback, this);

            String subject = Subjects.getNormalSubjects(this).get(Subjects.getSubjects(this).indexOf(i.getStringExtra("argument")))
                    .replaceAll(" ","%20");
            caller.withSubject(subject);

            caller.getArguments();
            swipe.post(new Runnable() {
                @Override public void run() {
                    swipe.setRefreshing(true);
                }
            });
        }else{
            if(Arguments.isArgumentSaved(this, i.getStringExtra("argument"))){
                ArrayList<Argument> list = Arguments.getSavedArguments(this, i.getStringExtra("argument"));
                populateList(list);
            }
        }
    }

    private void populateList(final ArrayList<Argument> list){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.reverse(list);
                mList.setAdapter(new ArgumentsActivityAdapter(ArgumentActivity.this, R.layout.model_argument_activity, list));
            }
        }, 500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default: return super.onOptionsItemSelected(menuItem);
        }
    }
}
