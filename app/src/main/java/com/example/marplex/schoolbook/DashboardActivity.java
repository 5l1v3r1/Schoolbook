package com.example.marplex.schoolbook;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.marplex.schoolbook.fragments.Agenda;
import com.example.marplex.schoolbook.fragments.Dashboard;
import com.example.marplex.schoolbook.fragments.Materie;
import com.example.marplex.schoolbook.fragments.Voti;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity{

    @Bind(R.id.toolbar) public Toolbar toolbar;
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.drawer) DrawerLayout drawer;
    @Bind(R.id.sliding_tabs) public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));

        //When the activity start, automatically replace R.id.frame with the Dashboard fragment
        Dashboard dashboard = new Dashboard();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, dashboard);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Dashboard");

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);
                        drawer.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.dashboard:

                                //Change activity color
                                if (Build.VERSION.SDK_INT >= 21) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                }

                                //Replace R.id.frame with the Dashboard fragment
                                Dashboard dashboard = new Dashboard();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, dashboard);
                                fragmentTransaction.commit();

                                getSupportActionBar().setTitle("Dashboard");
                                return true;
                            case R.id.voti:

                                //Change activity color
                                if (Build.VERSION.SDK_INT >= 21) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkGreen));
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                                }

                                //Replace R.id.frame with the Voti fragment
                                FragmentTransaction votiTransaction = getSupportFragmentManager().beginTransaction();
                                Voti voti = new Voti();
                                votiTransaction.replace(R.id.frame, voti);
                                votiTransaction.commit();

                                getSupportActionBar().setTitle("Voti");
                                return true;
                            case R.id.materie:

                                //Change activity color
                                if (Build.VERSION.SDK_INT >= 21) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkOrange));
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryOrange));
                                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryOrange));
                                }

                                //Replace R.id.frame with the Voti fragment
                                FragmentTransaction materieTransaction = getSupportFragmentManager().beginTransaction();
                                Materie materie = new Materie();
                                materieTransaction.replace(R.id.frame, materie);
                                materieTransaction.commit();

                                getSupportActionBar().setTitle("Materie");
                                return true;
                            case R.id.agenda:

                                //Change activity color
                                if (Build.VERSION.SDK_INT >= 21) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkTeal));
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTeal));
                                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTeal));
                                }

                                //Replace R.id.frame with the Agenda fragment
                                FragmentTransaction agendaTransaction = getSupportFragmentManager().beginTransaction();
                                Agenda agenda = new Agenda();
                                agendaTransaction.replace(R.id.frame, agenda);
                                agendaTransaction.commit();

                                getSupportActionBar().setTitle("Agenda");
                                return true;
                            case R.id.circolari:
                                return true;
                            default:
                                return true;
                        }
                    }
                });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.getMenu().getItem(0).setChecked(true);
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
