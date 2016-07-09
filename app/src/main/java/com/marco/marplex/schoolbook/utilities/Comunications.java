package com.marco.marplex.schoolbook.utilities;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marco.marplex.schoolbook.models.Comunication;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by marco on 5/24/16.
 */

public class Comunications {
    public static ArrayList<Comunication> getSavedComunications(Context c){
        Type type = new TypeToken<ArrayList<Comunication>>(){}.getType();
        return new Gson().fromJson(SharedPreferences.loadString(c, "datas", "circolari"), type);
    }

    public static void saveComunications(Context c, ArrayList<Comunication> comunications){
        SharedPreferences.saveString(c, "datas", "circolari", new Gson().toJson(comunications));
    }
    public static boolean isComunicationsSaved(Context c){
        return SharedPreferences.loadString(c, "datas", "circolari") != null ? true : false;
    }
}
