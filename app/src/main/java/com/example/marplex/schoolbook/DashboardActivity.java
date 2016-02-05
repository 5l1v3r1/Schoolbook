package com.example.marplex.schoolbook;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.marplex.schoolbook.fragments.Dashboard;
import com.example.marplex.schoolbook.fragments.Voti;

public class DashboardActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private Fragment fragment;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();fragment = new Dashboard();fragmentTransaction.replace(R.id.frame, fragment);fragmentTransaction.commit();getSupportActionBar().setTitle("Dashboard");

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);
                        drawer.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.dashboard:
                                FragmentTransaction dashboardTransaction = getSupportFragmentManager().beginTransaction();
                                Dashboard dashboard = new Dashboard();
                                dashboardTransaction.replace(R.id.frame, dashboard);
                                dashboardTransaction.commit();

                                getSupportActionBar().setTitle("Dashboard");
                                menuItem.setChecked(true);
                                return true;
                            case R.id.voti:
                                FragmentTransaction votiTransaction = getSupportFragmentManager().beginTransaction();
                                Voti voti = new Voti();
                                votiTransaction.replace(R.id.frame, voti);
                                votiTransaction.commit();

                                getSupportActionBar().setTitle("Voti");
                                menuItem.setChecked(true);
                                return true;
                            case R.id.verifiche:
                                break;
                            case R.id.circolari:
                                break;
                            default:
                                break;
                        }
                        drawer.closeDrawers();
                        return false;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)  return true;
        else if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }return super.onOptionsItemSelected(item);
    }


}
