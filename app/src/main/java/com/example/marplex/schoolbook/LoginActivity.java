package com.example.marplex.schoolbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.marplex.schoolbook.connections.ClassevivaAPI;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.simbio.encryption.Encryption;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_codice_utente) EditText utente;
    @Bind(R.id.input_password) EditText password;
    @Bind(R.id.name) TextInputLayout n;
    @Bind(R.id.pw) TextInputLayout p;
    @Bind(R.id.fabProgressCircle) FABProgressCircle progress;
    @Bind(R.id.fab) FloatingActionButton fab;

    String user,pass,cookies;
    String name, pw;

    Encryption encryption;

    classeViva callback;
    ClassevivaAPI login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Create an instance of Encrytion
        encryption = Encryption.getDefault("Key", "Salt", new byte[16]);

        // Load user's credentials with SharedPreferences help class
        user = SharedPreferences.loadString(this, "user", "user");
        pass = SharedPreferences.loadString(this, "user", "password");
        cookies = SharedPreferences.loadString(this, "user", "session");

        //Create the callback for ClassevivaAPI class
        callback = new classeViva() {
            @Override
            public void onPageLoaded(String html) {
                //If there's some error in login (Check if the <title> tag contains the login page title)
                if(html.contains("La Scuola del futuro, oggi")){
                    progress.hide();
                    if(html.contains("Password errata")){
                        // Error for wrong password
                    }else if(html.contains("Nome utente errato")){
                        // Error for wrong name
                    }
                }
                //Succesfull login
                else {
                    progress.beginFinalAnimation();
                    //If the user doesn't log in
                    if(user==null && pass==null && cookies==null){
                        //Save the new credentials
                        SharedPreferences.saveString(LoginActivity.this, "user", "user", name);
                        try {
                            //Use Encryption to cript the password
                            String encryptedPassword = encryption.encrypt(pw);
                            SharedPreferences.saveString(LoginActivity.this, "user", "password", encryptedPassword);
                        } catch (Exception e) {}
                        SharedPreferences.saveString(LoginActivity.this, "user", "session", new Gson().toJson(login.getSession()));
                    }

                    //Start DashboardActivity
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        };

        //Check if the user already has saved his credentials
        if(user!=null && pass!=null && cookies!=null){
            //Start DashboardActivity
            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
        }

        progress.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                progress.show();

                //name and pw now have the relative edittexts values
                name = utente.getText().toString();
                pw = password.getText().toString();

                //Create an instance of ClassevivaAPI
                login = new ClassevivaAPI(name, pw, callback, LoginActivity.this);
                //Perform login which return its  value in the callback
                login.doLogin();

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
