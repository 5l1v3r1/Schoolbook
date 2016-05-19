package com.marco.marplex.schoolbook;

/**
 * Created by marco on 1/30/16.
 */

public class Globals{
    private static String cookies;
    private static Globals instance;

    private Globals(){}

    public void setSession(String api){
        cookies = api;
    }
    public String getSession(){
        return cookies;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}
