package com.example.marplex.schoolbook.utilities;

import android.content.Context;

import com.example.marplex.schoolbook.models.Evento;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by marco on 5/8/16.
 */
public class Events {
    public static ArrayList<Evento> getEventsByDate(ArrayList<Evento> list, Date date){
        ArrayList<Evento> tmp = new ArrayList<>();
        for (Evento evento : list) {
            if(evento.date.equals(date)) tmp.add(evento);
        }
        return tmp;
    }
    public static String[] getAuthorsByDate(ArrayList<Evento> list, Date date){
        ArrayList<String> tmp = new ArrayList<>();
        for (Evento evento : list) {
            if(evento.date.equals(date)) tmp.add(evento.autore);
        }

        return tmp.toArray(new String[tmp.size()]);
    }
    public static String[] getTextesByDate(ArrayList<Evento> list, Date date){
        ArrayList<String> tmp = new ArrayList<>();
        for (Evento evento : list) {
            if(evento.date.equals(date)) tmp.add(evento.testo);
        }

        return tmp.toArray(new String[tmp.size()]);
    }

    public static ArrayList<Evento> getSavedEvents(Context c){
        Type type = new TypeToken<ArrayList<Evento>>(){}.getType();
        return new Gson().fromJson(SharedPreferences.loadString(c, "datas", "eventi"), type);
    }

    public static void saveEvents(Context c, ArrayList<Evento> eventi){
        SharedPreferences.saveString(c, "datas", "eventi", new Gson().toJson(eventi));
    }

    public static boolean isThereAnyEvents(Context c){
        return SharedPreferences.loadString(c, "datas", "eventi") != null ? true : false;
    }
}
