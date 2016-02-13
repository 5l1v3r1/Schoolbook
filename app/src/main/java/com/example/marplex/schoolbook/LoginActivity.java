package com.example.marplex.schoolbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.marplex.schoolbook.connections.ClassevivaAPI;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import se.simbio.encryption.Encryption;


public class LoginActivity extends AppCompatActivity {
    EditText utente, password;
    classeViva callback;
    ClassevivaAPI login;

    String name, pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);

        callback = new classeViva() {
            @Override
            public void onPageLoaded(String html) {

                SharedPreferences.saveString(LoginActivity.this, "user", "user", name);
                try {
                    SharedPreferences.saveString(LoginActivity.this, "user", "password", encryption.encrypt(pw));
                } catch (Exception e){}
                SharedPreferences.saveString(LoginActivity.this, "user", "session", new Gson().toJson(login.getSession()));

                Globals.getInstance().setSession(login.getSession());
                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        };

        String user = SharedPreferences.loadString(this, "user", "user");
        String pass = SharedPreferences.loadString(this, "user", "password");
        String cookies = SharedPreferences.loadString(this, "user", "session");

        if(user!=null && pass!=null && cookies!=null){

            System.out.println("TEST");

            String decrypted = encryption.decryptOrNull(pass);

            HashMap<String, String> session = new Gson().fromJson(cookies, new TypeToken<HashMap<String, String>>(){}.getType());

            login = new ClassevivaAPI(user, decrypted, callback, session);
            login.doLogin();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        utente = (EditText) findViewById(R.id.input_codice_utente);
        password = (EditText) findViewById(R.id.input_password);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
