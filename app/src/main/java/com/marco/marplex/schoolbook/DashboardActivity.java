package com.marco.marplex.schoolbook;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.fragments.AboutFragment;
import com.marco.marplex.schoolbook.fragments.Agenda;
import com.marco.marplex.schoolbook.fragments.ArgumentsFragment;
import com.marco.marplex.schoolbook.fragments.Circolari;
import com.marco.marplex.schoolbook.fragments.Dashboard;
import com.marco.marplex.schoolbook.fragments.Materie;
import com.marco.marplex.schoolbook.fragments.NoteFragment;
import com.marco.marplex.schoolbook.fragments.Voti;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.receivers.NotificationEventReceiver;
import com.marco.marplex.schoolbook.services.NotificationIntentService;
import com.marco.marplex.schoolbook.utilities.Comunications;
import com.marco.marplex.schoolbook.utilities.Connection;
import com.marco.marplex.schoolbook.utilities.Events;
import com.marco.marplex.schoolbook.utilities.Notes;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity{

    @Bind(R.id.toolbar) public Toolbar toolbar;
    @Bind(R.id.sliding_tabs) public TabLayout tabLayout;
    @Bind(R.id.nav_view) public NavigationView navigationView;
    @Bind(R.id.drawer) DrawerLayout drawer;

    private boolean mCanSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String theme = SharedPreferences.loadString(this, "pref", "setting_theme");
        if(theme.equals("Chiaro"))
            setTheme(R.style.AppTheme);
        else if(theme.equals("Scuro")){
            setTheme(R.style.AppThemeBlack);
        }

        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));

        //When the activity start, automatically replace R.id.frame with the Dashboard fragment
        setContainerFragment(new Dashboard());

        //Set username and school at the drawer header
        View drawerHeader = navigationView.getHeaderView(0);

        //Find views
        final TextView txtSchool = (TextView) drawerHeader.findViewById(R.id.txt_school);
        final TextView txtUsername = (TextView) drawerHeader.findViewById(R.id.txt_username);

        //Bind datas
        if(SharedPreferences.loadString(this, "datas", "userName") == null) {
            ClassevivaCaller caller = new ClassevivaCaller(new ClassevivaCallback<String[]>() {
                @Override
                public void onResponse(ArrayList<String[]> list) {
                    String[] array = list.get(0);

                    final String school = array[0];
                    final String user = array[1];

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtSchool.setText(school);
                            txtUsername.setText(user);
                        }
                    });

                    SharedPreferences.saveString(DashboardActivity.this, "datas", "userName", user);
                    SharedPreferences.saveString(DashboardActivity.this, "datas", "userSchool", school);
                }
            }, this);
            caller.getUser();
        }else{
            //Load from local
            String user = SharedPreferences.loadString(this, "datas", "userName");
            String school = SharedPreferences.loadString(this, "datas", "userSchool");

            txtSchool.setText(school);
            txtUsername.setText(user);
        }

        //Just start the IntentService for votes notification is it's not already started
        if(!isMyServiceRunning(NotificationIntentService.class)){
            if(SharedPreferences.loadBoolean(this, "pref", "setting_notification")){
                NotificationEventReceiver.setupAlarm(getApplicationContext());
            }
        }

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

                                    //Replace R.id.frame with the Materie fragment
                                    setContainerFragment(new Materie());

                                    return true;
                                case R.id.argomenti:

                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimaryPurple, R.color.colorPrimaryDarkPurple);

                                    //Replace R.id.frame with the ArgumentsFragment fragment
                                    setContainerFragment(new ArgumentsFragment());

                                    return true;
                                case R.id.agenda:

                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimaryTeal, R.color.colorPrimaryDarkTeal);

                                    //Replace R.id.frame with the Agenda fragment
                                    setContainerFragment(new Agenda());

                                    return true;
                                case R.id.circolari:

                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimaryRed, R.color.colorPrimaryDarkRed);

                                    //Replace R.id.frame with the Circolari fragment
                                    setContainerFragment(new Circolari());

                                    return true;
                                case R.id.note:
                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimaryAmber, R.color.colorPrimaryDarkAmber);

                                    //Replace R.id.frame with the NoteFragment fragment
                                    setContainerFragment(new NoteFragment());

                                    return true;
                                case R.id.about:
                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimary, R.color.colorPrimaryDark);

                                    //Replace R.id.frame with the AboutFragment fragment
                                    setContainerFragment(new AboutFragment());
                                    return true;
                                case R.id.feedback:
                                    //Change activity color
                                    changeActivityColor(R.color.colorPrimary, R.color.colorPrimaryDark);

                                    //Start FeedbackActivity activity
                                    startActivity(new Intent(DashboardActivity.this, FeedbackActivity.class));
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

        boolean canIupdate = SharedPreferences.loadBoolean(this, "pref", "setting_sync");
        if(canIupdate) {
            if(Connection.isNetworkAvailable(this)) {
                //Start downloading votes from Classeviva
                ClassevivaCaller callerVotes = new ClassevivaCaller(new ClassevivaCallback() {
                    @Override
                    public void onResponse(final ArrayList list) {
                        DashboardActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Votes.saveVotes(DashboardActivity.this, list);
                                mCanSelect = true;
                            }
                        });
                    }
                }, this);
                callerVotes.getVotes();

                //Start downloading events from Classeviva
                ClassevivaCaller callerAgenda = new ClassevivaCaller(new ClassevivaCallback() {
                    @Override
                    public void onResponse(final ArrayList list) {
                        DashboardActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Events.saveEvents(DashboardActivity.this, list);
                                mCanSelect = true;
                            }
                        });
                    }
                }, this);
                callerAgenda.getAgenda();

                //Start downloading comunications from Classeviva
                ClassevivaCaller callerComunication = new ClassevivaCaller(new ClassevivaCallback() {
                    @Override
                    public void onResponse(final ArrayList list) {
                        DashboardActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Comunications.saveComunications(DashboardActivity.this, list);
                                mCanSelect = true;
                            }
                        });
                    }
                }, this);

                callerComunication.getSchoolComunication();

                //Start downloading notes from Classeviva
                ClassevivaCaller callerNotes = new ClassevivaCaller(new ClassevivaCallback() {
                    @Override
                    public void onResponse(final ArrayList list) {
                        DashboardActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Notes.saveNotes(DashboardActivity.this, list);
                                mCanSelect = true;

                                Snackbar.make(navigationView, "Finito", Snackbar.LENGTH_SHORT).show();


                                //Refresh fragment
                                if(SharedPreferences.loadBoolean(DashboardActivity.this, "pref", "first"))
                                    setContainerFragment(new Dashboard());

                                SharedPreferences.saveBoolean(DashboardActivity.this, "pref", "first", true);
                            }
                        });
                    }
                }, this);

                callerNotes.getNotes();
                Snackbar.make(navigationView, "Sto aggiornando il tuo registro...", Snackbar.LENGTH_INDEFINITE).show();
            }else{
                mCanSelect = true;
            }
        }else{
            mCanSelect = true;
        }
    }

    /**
     * Change the activity base color(statusbar, toolbar, navigationbar, tabs and navigation drawer's header)
     *
     * @param colorPrimary Activity primary color
     * @param colorPrimaryDark Activity primary dark color
     *
     */
    public void changeActivityColor(int colorPrimary, int colorPrimaryDark){
        if (Build.VERSION.SDK_INT >= 21){
            //Statusbar color
            getWindow().setStatusBarColor(ContextCompat.getColor(DashboardActivity.this, colorPrimaryDark));

            //Navigation bar color
            getWindow().setNavigationBarColor(ContextCompat.getColor(DashboardActivity.this, colorPrimary));
        }

        //Toolbar background color
        toolbar.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, colorPrimary));

        //TabLayout background color
        tabLayout.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, colorPrimary));

        //Navigation drawer's header background color
        //navigationView.getRootView().findViewById(R.id.header_bg).setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, colorPrimary));
    }

    /**
     * Inflate a fragment in R.id.frame view
     *
     * @param fragment The inflated fragment
     * @see DrawerFragment
     *
     */
    public void setContainerFragment(DrawerFragment fragment){
        //Replace R.id.frame with fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();

        //Set toolbar title
        getSupportActionBar().setTitle(fragment.getTitle());
    }

    //Open Schoolbook's GitHub page
    public void showGithub(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Marplex/Schoolbook"));
        startActivity(intent);
    }

    //Open Schoolbook's PlayStore page
    public void showPlayStore(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.marco.marplex.schoolbook"));
        startActivity(intent);
    }

    public void shareApp(View view){
        String message = "Che ne dici di dare un'occhiata a Schoolbook? Un nuovo registro elettronico non ufficiale per Classeviva, in Material Design, Open Source e con tante funzionalità."
                + "\n Scaricalo qui schbook.x10.mx";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(share, "Condividi con"));
    }


    //From http://stackoverflow.com/a/5921190
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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