package com.marco.marplex.schoolbook.utilities;

import android.content.Context;

/**
 * Created by marco on 3/8/16.
 */
public class Credentials {

    public static String getName(Context c){
        return SharedPreferences.loadString(c, "user", "user");
    }

    public static String getPassword(Context c){
        return SharedPreferences.loadString(c, "user", "password");
    }

    public static String getCode(Context c){
        return SharedPreferences.loadString(c, "user", "code");
    }

    public static String getSession(Context c){
        return SharedPreferences.loadString(c, "user", "sessionID");
    }

    public static void saveSession(Context c, String session){
        SharedPreferences.saveString(c, "user", "sessionID", session);
    }

    public static void saveCredentials(Context c, String name, String password, String code, String session) {
        SharedPreferences.saveString(c, "user", "user", name);
        SharedPreferences.saveString(c, "user", "password", Cripter.criptString(password) );
        SharedPreferences.saveString(c, "user", "sessionID", session);
        SharedPreferences.saveString(c, "user", "code", code);
    }

}
