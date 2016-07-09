package com.marco.marplex.schoolbook.utilities;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marco.marplex.schoolbook.models.Argument;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by marco on 03/07/16.
 */

public class Arguments {
    public static ArrayList<Argument> getSavedArguments(Context c, String subject){
        Type type = new TypeToken<ArrayList<Argument>>(){}.getType();
        return new Gson().fromJson(SharedPreferences.loadString(c, "argument", subject), type);
    }

    public static void saveArguments(Context c, ArrayList<Argument> arguments, String subject){
        SharedPreferences.saveString(c, "argument", subject, new Gson().toJson(arguments));
    }
    public static boolean isArgumentSaved(Context c, String subject){
        return SharedPreferences.loadString(c, "argument", subject) != null ? true : false;
    }
}
