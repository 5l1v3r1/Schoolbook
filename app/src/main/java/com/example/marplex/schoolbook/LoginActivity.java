package com.example.marplex.schoolbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.marplex.schoolbook.connections.ClassevivaAPI;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.utilities.Credentials;
import com.github.jorgecastilloprz.FABProgressCircle;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.simbio.encryption.Encryption;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_codice_utente) EditText mUtente;
    @Bind(R.id.input_password) EditText mPassword;
    @Bind(R.id.fabProgressCircle) FABProgressCircle mProgress;
    @Bind(R.id.fab) FloatingActionButton mFab;

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
                    mProgress.hide();

                    if(html.contains("Password errata")){
                        // Error for wrong password
                    }else if(html.contains("Nome utente errato")){
                        // Error for wrong name
                    }
                }
                //Succesfull login
                else {
                    mProgress.beginFinalAnimation();

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

        mProgress.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mProgress.show();

                //name and pw now have the relative edittexts values
                mName = mUtente.getText().toString();
                mPw = mPassword.getText().toString();

                //Create an instance of ClassevivaAPI
                mLogin = new ClassevivaAPI(mName, mPw, mCallback, LoginActivity.this);
                //Perform login which return its  value in the callback
                mLogin.doLogin();

            }
        });
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
