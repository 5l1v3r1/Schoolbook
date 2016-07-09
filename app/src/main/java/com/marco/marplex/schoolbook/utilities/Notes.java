package com.marco.marplex.schoolbook.utilities;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marco.marplex.schoolbook.models.Note;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by marco on 5/24/16.
 */

public class Notes {
    public static ArrayList<Note> getSavedNotes(Context c){
        Type type = new TypeToken<ArrayList<Note>>(){}.getType();
        return new Gson().fromJson(SharedPreferences.loadString(c, "datas", "note"), type);
    }

    public static void saveNotes(Context c, ArrayList<Note> notes){
        SharedPreferences.saveString(c, "datas", "note", new Gson().toJson(notes));
    }
    public static boolean isNotesSaved(Context c){
        return SharedPreferences.loadString(c, "datas", "note") != null ? true : false;
    }
}
