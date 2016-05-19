package com.example.marplex.schoolbook;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.marplex.schoolbook.connections.ClassevivaCaller;
import com.example.marplex.schoolbook.fragments.AboutFragment;
import com.example.marplex.schoolbook.fragments.Agenda;
import com.example.marplex.schoolbook.fragments.Dashboard;
import com.example.marplex.schoolbook.fragments.Materie;
import com.example.marplex.schoolbook.fragments.Voti;
import com.example.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.example.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.example.marplex.schoolbook.services.NotificationService;
import com.example.marplex.schoolbook.utilities.Connection;
import com.example.marplex.schoolbook.utilities.Events;
import com.example.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity{

    @Bind(R.id.toolbar) public Toolbar toolbar;
    @Bind(R.id.sliding_tabs) public TabLayout tabLayout;
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.drawer) DrawerLayout drawer;

    private boolean mCanSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));

        //When the activity start, automatically replace R.id.frame with the Dashboard fragment
        setContainerFragment(new Dashboard());

        //Just start the IntentService for votes notification
        startService(new Intent(getBaseContext(), NotificationService.class));

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        //Used it to prevent drawer panel non smooth close
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                drawer.closeDrawers();
                            }
                        }, 35);

                        if(mCanSelect) {
                            switch (menuItem.getItemId()) {
                                case R.id.dashboard:

                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimary, R.color.colorPrimaryDark);

                                    //Replace R.id.frame with the Dashboard fragment
                                    setContainerFragment(new Dashboard());

                                    return true;
                                case R.id.voti:

                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimaryGreen, R.color.colorPrimaryDarkGreen);

                                    //Replace R.id.frame with the Voti fragment
                                    setContainerFragment(new Voti());

                                    return true;
                                case R.id.materie:

                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimaryOrange, R.color.colorPrimaryDarkOrange);

                                    //Replace R.id.frame with the Voti fragment
                                    setContainerFragment(new Materie());

                                    return true;
                                case R.id.agenda:

                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimaryTeal, R.color.colorPrimaryDarkTeal);

                                    //Replace R.id.frame with the Agenda fragment
                                    setContainerFragment(new Agenda());

                                    return true;
                                case R.id.about:
                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimary, R.color.colorPrimaryDark);

                                    //Replace R.id.frame with the Agenda fragment
                                    setContainerFragment(new AboutFragment());
                                    return true;
                                case R.id.settings:
                                    //Open settings activity
                                    startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
                                    return true;
                                default:
                                    return true;
                            }
                        }else return true;
                    }
                });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.openDrawer, R.string.closeDrawer);

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.getMenu().getItem(0).setChecked(true);

        boolean canIupdate = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("setting_sync", true);
        if(canIupdate) {
            if(Connection.isNetworkAvailable(this)) {
                //Start downloading votes from Classeviva
                ClassevivaCaller caller = new ClassevivaCaller(new ClassevivaCallback() {
                    @Override
                    public void onResponse(final ArrayList list) {
                        DashboardActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Votes.saveVotes(DashboardActivity.this, list);
                                mCanSelect = true;

                                //Refresh fragment
                                setContainerFragment(new Dashboard());
                            }
                        });
                    }
                }, this);
                caller.getVotes();

                //Start downloading events from Classeviva
                ClassevivaCaller callerAgenda = new ClassevivaCaller(new ClassevivaCallback() {
                    @Override
                    public void onResponse(final ArrayList list) {
                        DashboardActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Events.saveEvents(DashboardActivity.this, list);
                                mCanSelect = true;

                                Snackbar.make(navigationView, "Finito", Snackbar.LENGTH_SHORT).show();

                                //Refresh fragment
                                setContainerFragment(new Dashboard());
                            }
                        });
                    }
                }, this);
                callerAgenda.getAgenda();
                Snackbar.make(navigationView, "Sto aggiornando il tuo registro...", Snackbar.LENGTH_INDEFINITE).show();
            }else{
                mCanSelect = true;
            }
        }else{
            mCanSelect = true;

            //Refresh fragment
            setContainerFragment(new Dashboard());
        }
    }

    /**
     * Change the activity base color(statusbar, toolbar, navigationbar, tabs and navigation drawer's header)
     *
     * @param colorPrimary Activity primary color
     * @param colorPrimaryDark Activity primary dark color
     *
     */
    private void changeActivityColor(int colorPrimary, int colorPrimaryDark){
        if (Build.VERSION.SDK_INT >= 21){
            //Statusbar color
            getWindow().setStatusBarColor(ContextCompat.getColor(DashboardActivity.this, colorPrimaryDark));

            //Navigation bar color
            getWindow().setNavigationBarColor(ContextCompat.getColor(DashboardActivity.this, colorPrimary));

            //Toolbar background color
            toolbar.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, colorPrimary));

            //TabLayout background color
            tabLayout.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, colorPrimary));

            //Navigation drawer's header background color
            navigationView.getRootView().findViewById(R.id.header_bg).setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, colorPrimary));
        }
    }

    /**
     * Inflate a fragment in R.id.frame view
     *
     * @param fragment The inflated fragment
     * @see DrawerFragment
     *
     */
    private void setContainerFragment(DrawerFragment fragment){
        //Replace R.id.frame with fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();

        //Set toolbar title
        getSupportActionBar().setTitle(fragment.getTitle());
    }

    //Open the GitHub page
    public void showGithub(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Marplex/Schoolbook"));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }return super.onOptionsItemSelected(item);
    }

    public void setMenu(int menu, Toolbar.OnMenuItemClickListener callback){
        toolbar.getMenu().clear();
        toolbar.inflateMenu(menu);
        toolbar.setOnMenuItemClickListener(callback);
    }
}