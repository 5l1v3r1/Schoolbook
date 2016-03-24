package com.example.marplex.schoolbook.utilities;

import android.content.Context;

import com.example.marplex.schoolbook.models.Voto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 3/23/16.
 */
public class Votes {
    public static ArrayList<Voto> getVotesByPeriod(ArrayList<Voto> voti, int period){
        ArrayList<Voto> votoArrayList = new ArrayList<>();
        for(Voto voto : voti){
            if(voto.periodo==period) votoArrayList.add(voto);
            else continue;
        }
        return votoArrayList;
    }

    public static ArrayList<Voto> getNumericalVotesByMateria(Context c, String materia, int period){
        ArrayList<Voto> tmp = Votes.getVotesByPeriod(c, period);

        ArrayList<Voto> voti = new ArrayList<>();
        for(Voto voto : tmp){
            if( !(voto.voto.equals("-") || voto.voto.equals("+") || voto.voto.equals("nav")) ){
                if(voto.materia.equals(materia)){
                    voti.add(voto);
                }
                else continue;
            }else continue;
        }
        return voti;

    }

    public static ArrayList<Voto> getVotesByMateria(Context c, String materia, int period){
        ArrayList<Voto> tmp = Votes.getVotesByPeriod(c, period);

        ArrayList<Voto> voti = new ArrayList<>();
        for(Voto voto : tmp){
            if(voto.materia.equals(materia)){
                voti.add(voto);
            }
            else continue;
        }
        return voti;

    }

    public static ArrayList<Voto> getVotesByPeriod(Context c, int period){
        Type type = new TypeToken<List<Voto>>(){}.getType();
        ArrayList<Voto> datas = new Gson().fromJson(SharedPreferences.loadString(c, "datas", "voti"), type);

        ArrayList<Voto> votoArrayList = new ArrayList<>();
        for(Voto voto : datas){
            if(voto.periodo==period) votoArrayList.add(voto);
            else continue;
        }
        return votoArrayList;
    }

    public static void saveVotes(Context c, ArrayList<Voto> voti){
        SharedPreferences.saveString(c, "datas", "voti", new Gson().toJson(voti));
    }

    public static ArrayList<Voto> getVotes(Context c){
        Type type = new TypeToken<List<Voto>>(){}.getType();
        return new Gson().fromJson(SharedPreferences.loadString(c, "datas", "voti"), type);
    }
}
