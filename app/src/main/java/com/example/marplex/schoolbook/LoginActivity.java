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
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import se.simbio.encryption.Encryption;


public class LoginActivity extends AppCompatActivity {
    EditText utente, password;
    classeViva callback;
    ClassevivaAPI login;
    FABProgressCircle progress;

    String name, pw;

    String user,pass,cookies;
    TextInputLayout n,p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        utente = (EditText) findViewById(R.id.input_codice_utente);
        password = (EditText) findViewById(R.id.input_password);

        n = (TextInputLayout) findViewById(R.id.name);
        p = (TextInputLayout) findViewById(R.id.pw);

        final Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);

        user = SharedPreferences.loadString(this, "user", "user");
        pass = SharedPreferences.loadString(this, "user", "password");
        cookies = SharedPreferences.loadString(this, "user", "session");

        callback = new classeViva() {
            @Override
            public void onPageLoaded(String html) {
                if(html.contains("La Scuola del futuro, oggi")){
                    progress.hide();
                    if(html.contains("Password errata")){
                        //p.setError("La password è errata");
                    }else if(html.contains("Nome utente errato")){
                        //n.setError("Nome utente errato");
                    }
                }else {
                    progress.beginFinalAnimation();
                    if(user==null && pass==null && cookies==null){

                        SharedPreferences.saveString(LoginActivity.this, "user", "user", name);
                        try {
                            SharedPreferences.saveString(LoginActivity.this, "user", "password", encryption.encrypt(pw));
                        } catch (Exception e) {
                        }
                        SharedPreferences.saveString(LoginActivity.this, "user", "session", new Gson().toJson(login.getSession()));
                    }

                    Globals.getInstance().setSession(login.getSession());
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        };



        if(user!=null && pass!=null && cookies!=null){
            String decrypted = encryption.decryptOrNull(pass);

            HashMap<String, String> session = new Gson().fromJson(cookies, new TypeToken<HashMap<String, String>>(){}.getType());

            login = new ClassevivaAPI(user, decrypted, callback, session);
            login.doLogin();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        progress = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        progress.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                progress.show();

                name = utente.getText().toString();
                pw = password.getText().toString();

                login = new ClassevivaAPI(name, pw, callback);
                login.doLogin();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
