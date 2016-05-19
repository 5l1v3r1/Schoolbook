package com.marco.marplex.schoolbook.utilities;

import android.content.Context;

import com.marco.marplex.schoolbook.models.Obiettivo;
import com.google.gson.Gson;

/**
 * Created by marco on 4/23/16.
 */
public class ObjectiveUtil {
    public static Obiettivo getObiettivoByMateria(Context c, String materia, int period){
        return new Gson().fromJson(SharedPreferences.loadString(c, "objective", materia+"_"+period), Obiettivo.class);
    }
    public static void setObiettivo(Context c, String materia, int period, Obiettivo obiettivo){
        SharedPreferences.saveString(c, "objective", materia+"_"+period, new Gson().toJson(obiettivo));
    }
    public static boolean isObiettivoSaved(Context c, String materia, int period){
        return SharedPreferences.loadString(c, "objective", materia+"_"+period) != null ? true : false;
    }
    public static void removeObiettivo(Context c, String materia, int period){
        SharedPreferences.remove(c, "objective", materia+"_"+period);
    }
}
