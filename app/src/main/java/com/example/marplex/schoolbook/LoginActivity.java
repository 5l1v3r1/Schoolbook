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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class LoginActivity extends AppCompatActivity {
    EditText utente, password;
    classeViva callback;
    ClassevivaAPI login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(i);
        finish();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        utente = (EditText) findViewById(R.id.input_codice_utente);
        password = (EditText) findViewById(R.id.input_password);
        setSupportActionBar(toolbar);

        callback = new classeViva() {
            @Override
            public void onPageLoaded(String html) {
                Document doc = Jsoup.parse(html);
                Globals.getInstance().setSession(login.getSession());
                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login = new ClassevivaAPI(utente.getText().toString(), password.getText().toString(), callback);
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
