package com.marco.marplex.schoolbook;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marco.marplex.schoolbook.adapters.SettingInputAdapter;
import com.marco.marplex.schoolbook.models.SettingHeader;
import com.marco.marplex.schoolbook.views.StaticHeightListView;

import java.util.ArrayList;

public abstract class BaseSettingActivity extends AppCompatActivity {


    private ArrayList<SettingHeader> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Impostazioni");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.setOrientation(LinearLayout.VERTICAL);

        NestedScrollView scrollView = new NestedScrollView(this);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.addView(container);

        headers = new ArrayList();
        onInit();

        //Start parsing and creating the layout
        for(SettingHeader header : headers){
            CardView headerCard = (CardView) LayoutInflater.from(this).inflate(R.layout.settings_header, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 10);
            headerCard.setLayoutParams(params);

            TextView headerTitle = (TextView) headerCard.findViewById(R.id.headerTitle);
            StaticHeightListView inputsContainer = (StaticHeightListView) headerCard.findViewById(R.id.inputContainer);

            headerTitle.setText(header.title);

            inputsContainer.setAdapter(new SettingInputAdapter(this, header.inputs));

            container.addView(headerCard);
        }

        setContentView(scrollView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default: return super.onOptionsItemSelected(menuItem);
        }
    }

    public abstract void onInit();

    protected void addHeader(SettingHeader header){
        headers.add(header);
    }

}
