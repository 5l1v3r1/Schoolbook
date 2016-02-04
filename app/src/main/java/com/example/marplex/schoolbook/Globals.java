package com.example.marplex.schoolbook;

/**
 * Created by marco on 1/30/16.
 */

import java.util.Map;

public class Globals{
    private static Map<String, String> cookies;
    private static Globals instance;

    private Globals(){}

    public void setSession(Map<String, String> api){
        cookies = api;
    }
    public Map<String, String> getSession(){
        return cookies;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}
