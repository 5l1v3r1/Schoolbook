package com.example.marplex.schoolbook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import com.example.marplex.schoolbook.fragments.Voti;

public class DashboardActivity extends AppCompatActivity{

    public Toolbar toolbar;
    private Fragment fragment;
    private DrawerLayout drawer;

    public TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));


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
                                Dashboard dashboard = new Dashboard();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, dashboard);
                                fragmentTransaction.commit();

                                getSupportActionBar().setTitle("Dashboard");
                                return true;
                            case R.id.voti:
                                FragmentTransaction votiTransaction = getSupportFragmentManager().beginTransaction();
                                Voti voti = new Voti();
                                votiTransaction.replace(R.id.frame, voti);
                                votiTransaction.commit();

                                getSupportActionBar().setTitle("Voti");
                                return true;
                            case R.id.verifiche:
                                FragmentTransaction agendaTransaction = getSupportFragmentManager().beginTransaction();
                                Agenda agenda = new Agenda();
                                agendaTransaction.replace(R.id.frame, agenda);
                                agendaTransaction.commit();

                                getSupportActionBar().setTitle("Voti");
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
