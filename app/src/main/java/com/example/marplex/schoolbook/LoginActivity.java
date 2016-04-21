package com.example.marplex.schoolbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marplex.schoolbook.connections.ClassevivaAPI;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.utilities.Credentials;
import com.example.marplex.schoolbook.utilities.WindowUtil;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import se.simbio.encryption.Encryption;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_codice_utente) EditText mUtente;
    @Bind(R.id.input_password) EditText mPassword;
    @Bind(R.id.fabLogin) FloatingActionButton mFabLogin;
    @Bind(R.id.login_title) TextView mTitle;
    @Bind(R.id.red_bg) RelativeLayout reveal;
    @Bind(R.id.txt_loading) TextView mLoadingTextView;
    @Bind(R.id.progress) DilatingDotsProgressBar mProgress;
    @Bind(R.id.name) TextInputLayout mNameLayout;
    @Bind(R.id.pw) TextInputLayout mPwLayout;

    String mUser,mPass, mSession;
    String mName, mPw;

    Encryption mEncryption;

    classeViva mCallback;
    ClassevivaAPI mLogin;

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

        // Create an instance of Encrytion
        mEncryption = Encryption.getDefault("Key", "Salt", new byte[16]);

        // Load user's credentials with SharedPreferences help class
        mUser = Credentials.getName(this);
        mPass = Credentials.getPassword(this);
        mSession = Credentials.getSession(this);

        //Create the callback for ClassevivaAPI class
        mCallback = new classeViva() {
            @Override
            public void onPageLoaded(String html) {
                //If there's some error in login (Check if the <title> tag contains the login page title)
                if(html.contains("La Scuola del futuro, oggi")){
                    animateReverseFab();
                }
                //Succesfull login
                else {

                    //If the user doesn't log in
                    if(mUser==null && mPass==null && mSession ==null){
                        //Save the new credentials
                        Credentials.saveCredentials(LoginActivity.this, mName, mPw, mLogin.getSession() );
                    }

                    //Start DashboardActivity
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        };

        //Check if the user already has saved his credentials
        if(mUser!=null && mPass!=null && mSession !=null){
            //Start DashboardActivity
            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
        }

        mFabLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                //Animations
                animateFab();

                //Name and pw now have the relative edittexts values
                mName = mUtente.getText().toString();
                mPw = mPassword.getText().toString();

                //Create an instance of ClassevivaAPI
                mLogin = new ClassevivaAPI(mName, mPw, mCallback, LoginActivity.this);
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