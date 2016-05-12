package com.example.marplex.schoolbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.marplex.schoolbook.adapters.CompareAdapter;
import com.example.marplex.schoolbook.utilities.Subjects;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CompareActivity extends AppCompatActivity {

    @Bind(R.id.rv_materieCompare) RecyclerView mMainRecyclerView;
    @Bind(R.id.spinner_compare) Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMainRecyclerView.setHasFixedSize(true);
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainRecyclerView.setAdapter(new CompareAdapter(this, Subjects.getComparedSubjects(this)));

        final ArrayList<String> list = Subjects.getSubjects(this);
        list.add(0, "Seleziona una materia");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(R.layout.dropdown_item);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    mMainRecyclerView.setAdapter(new CompareAdapter(getApplicationContext(), Subjects.getComparedSubject(getApplicationContext(), list.get(i))));
                }else{
                    mMainRecyclerView.setAdapter(new CompareAdapter(getApplicationContext(), Subjects.getComparedSubjects(getApplicationContext())));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
