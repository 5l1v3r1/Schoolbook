package com.example.marplex.schoolbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Materia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView scritto = (TextView)findViewById(R.id.votoScritto);
        TextView orale = (TextView)findViewById(R.id.votoOrale);
        TextView media = (TextView)findViewById(R.id.mediaVoti);
        CircularProgressBar progress = (CircularProgressBar) findViewById(R.id.view);

        Bundle b = getIntent().getExtras();
        String title = b.getString("materia");
        int periodo = b.getInt("periodo");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Type type = new TypeToken<List<Voto>>(){}.getType();
        List<Voto> datas = new Gson().fromJson(SharedPreferences.loadString(this, "datas", "voti"), type);

        List<Voto> voti = new ArrayList<>();
        for(int i=0; i<datas.size();i++){
            if(datas.get(i).periodo==periodo) {
                voti.add(datas.get(i));
                System.out.println(datas.get(i).periodo);
            }
            else continue;
        }datas.clear();

        List<Voto> materiaVoti = new ArrayList<>();
        for(int i=0; i<voti.size();i++){
            if(voti.get(i).materia.equals(title)){
                if( !(voti.get(i).voto.equals("-") || voti.get(i).voto.equals("+") || voti.get(i).voto.equals("nav")) ){
                    materiaVoti.add(voti.get(i));
                }
            }
            else continue;
        }voti.clear();

        double sumScritto = 0;
        int scrittoTimes = 0;
        double sumOrale = 0;
        int oraleTimes = 0;
        double sum = 0;
        for(int i=0; i<materiaVoti.size(); i++){
            if(materiaVoti.get(i).tipo.equals("Scritto") || materiaVoti.get(i).tipo.equals("Pratico")){
                sumScritto += getVoto(materiaVoti.get(i).voto);
                System.out.println(getVoto(materiaVoti.get(i).voto));
                scrittoTimes++;
            }else{
                sumOrale += getVoto(materiaVoti.get(i).voto);
                System.out.println(getVoto(materiaVoti.get(i).voto));
                oraleTimes++;
            }
            sum += getVoto(materiaVoti.get(i).voto);
            System.out.println(getVoto(materiaVoti.get(i).voto));
        }

        double totalAverage = sum/materiaVoti.size();
        double scrittoAverage = sumScritto/scrittoTimes;
        double oraleAverage = sumOrale/oraleTimes;

        System.out.println(totalAverage);
        System.out.println(scrittoAverage);
        System.out.println(oraleAverage);

        scritto.setText(""+arrotondaRint(scrittoAverage, 2));
        orale.setText("" + arrotondaRint(oraleAverage, 2));
        media.setText("" + arrotondaRint(totalAverage, 2));

        progress.setProgress((int)totalAverage*10);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    double getVoto(String val){

        if(val.length()==1){
            if(val.contains("+")) return 0;
            else if(val.contains("-"))return 0;
            else return Double.parseDouble(val);
        }else if(val.length()==2){
            if(val.endsWith("½")) {
                String value = val.substring(0, val.length()-1);
                return Double.parseDouble(value) + 0.5;
            }else if(val.endsWith("-")){
                String value = val.substring(0, val.length()-1);
                return Double.parseDouble(value) - 0.15;
            }else if(val.endsWith("+")){
                String value = val.substring(0, val.length()-1);
                return Double.parseDouble(value) + 0.15;
            }else if(val.equals("10")){
                return 10;
            }
        }else if(val.length()==3){
            if(val.endsWith("-")){
                String value = val.substring(0, val.length()-1);
                return Double.parseDouble(value) - 0.15;
            }else return 0;
        }

        return 0;
    }

    public static double arrotondaRint(double value, int numCifreDecimali) {
        double temp = Math.pow(10, numCifreDecimali);
        return Math.rint(value * temp) / temp;
    }

}
