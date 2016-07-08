package com.marco.marplex.schoolbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.interfaces.ClassevivaLoginCallback;
import com.marco.marplex.schoolbook.utilities.Connection;
import com.marco.marplex.schoolbook.utilities.Credentials;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;
import com.marco.marplex.schoolbook.utilities.WindowUtil;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_codice_utente) EditText mUtente;
    @Bind(R.id.input_password) EditText mPassword;
    @Bind(R.id.fabLogin) FloatingActionButton mFabLogin;
    @Bind(R.id.login_title) TextView mTitle;
    @Bind(R.id.red_bg) RelativeLayout reveal;
    @Bind(R.id.txt_loading) TextView mLoadingTextView;
    @Bind(R.id.progress) DilatingDotsProgressBar mProgress;

    String mUser,mPass, mSession;
    String mName, mPw;

    ClassevivaLoginCallback mCallback;
    ClassevivaCaller mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Animations!
        mTitle.setY(mTitle.getY()+mTitle.getHeight()+50);
        mTitle.setAlpha(0.1f);
        mTitle.animate()
                .translationYBy(-(mTitle.getHeight()+50))
                .alpha(1F)
                .setDuration(500L)
                .start();

        // Load user's credentials with SharedPreferences help class
        mUser = Credentials.getName(this);
        mPass = Credentials.getPassword(this);
        mSession = Credentials.getSession(this);

        //Create the callback for ClassevivaCaller class
        mCallback = new ClassevivaLoginCallback() {
            @Override
            public void onLoginDone(boolean success) {
                //If there's some error in login (Check if the <title> tag contains the login page title)
                if(!success){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            animateReverseFab();
                        }
                    });
                }else {
                    //Start AppIntroActivity and DashboardActivity
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    Intent i2 = new Intent(LoginActivity.this, AppIntroActivity.class);
                    startActivity(i);
                    startActivity(i2);
                    finish();
                }
            }
        };

        //Check if the user already has saved his credentials
        if(mUser!=null && mPass!=null && mSession !=null){

            //First of all, get a new session if it's possible
            if(Connection.isNetworkAvailable(this)){
                Thread separateThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /*new ClassevivaCaller(new ClassevivaCallback() {
                            @Override public void onResponse(ArrayList list) { }
                        }, LoginActivity.this).newSession();*/
                    }
                });
                separateThread.start();
            }

            if(!SharedPreferences.loadBoolean(this, "pref", "first")) {
                //Start AppIntroActivity
                SharedPreferences.saveBoolean(this, "pref", "first", true);
                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                Intent i2 = new Intent(LoginActivity.this, AppIntroActivity.class);
                startActivity(i);
                startActivity(i2);
                finish();
            }else{
                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        }

        mFabLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                //Animations
                animateFab();

                //Name and pw now have the relative edittexts values
                mName = mUtente.getText().toString();
                mPw = mPassword.getText().toString();

                //Create an instance of ClassevivaCaller
                mLogin = new ClassevivaCaller(mName, mPw, mCallback, LoginActivity.this);
                //Perform login which return its  value in the callback
                mLogin.doLogin();

            }
        });
    }

    private void animateFab(){

        startReveal(reveal, false);

        mFabLogin.animate()
                .alpha(0f)
                .setDuration(250L)
                .start();

        mLoadingTextView.setY(mLoadingTextView.getY()+mLoadingTextView.getHeight()+50);
        mLoadingTextView.setAlpha(0.1f);
        mLoadingTextView.animate()
                .translationYBy(-(mLoadingTextView.getHeight()+50))
                .alpha(1F)
                .setDuration(500L)
                .start();

        mProgress.setY(mProgress.getY()+mProgress.getHeight()+50);
        mProgress.setAlpha(0.1f);
        mProgress.animate()
                .translationYBy(-(mProgress.getHeight()+50))
                .alpha(1F)
                .setDuration(500L)
                .start();

        mProgress.setDotSpacing(30);
        mProgress.setGrowthSpeed(670);
        mProgress.showNow();
    }

    private void animateReverseFab(){
        mFabLogin.animate()
                .alpha(1f)
                .setDuration(500L)
                .start();
        startReveal(reveal, true);
    }

    private void startReveal(final RelativeLayout view, boolean reverse){

        //Change statusbar color
        if(!reverse) WindowUtil.changeStatusBarColor(LoginActivity.this, "#E91E63"); //Need to put string because resource link doesn't work
        else WindowUtil.changeStatusBarColor(LoginActivity.this, "#1976D2");

        // Get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = view.getBottom() - 60;

        // Get the final radius for the clipping circle
        int dx = Math.max(cx, view.getWidth() - cx);
        int dy = Math.max(cy, view.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        view.setVisibility(View.VISIBLE);

        SupportAnimator animator =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        if(reverse) {
            animator = animator.reverse();
            animator.addListener(new SupportAnimator.AnimatorListener() {
                @Override public void onAnimationEnd() { view.setVisibility(View.INVISIBLE); }
                @Override public void onAnimationCancel() {}
                @Override public void onAnimationRepeat() {}
                @Override public void onAnimationStart() {}
            });
        }
        animator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}