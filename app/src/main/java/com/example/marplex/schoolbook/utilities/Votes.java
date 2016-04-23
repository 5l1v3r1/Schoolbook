package com.example.marplex.schoolbook.utilities;

import android.content.Context;
import android.graphics.Color;

import com.example.marplex.schoolbook.models.Voto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    public static ArrayList<Voto> getVotesByRecentDate(Context c, int daysFromNow){
        ArrayList<Voto> votes = Votes.getVotes(c);
        ArrayList<Voto> tmp = new ArrayList<>();
        for(Voto voto : votes){
            String date;
            if(voto.periodo == 1) date = voto.data + "/" + (new GregorianCalendar().get(Calendar.YEAR)-1);
            else date = voto.data + "/" + (new GregorianCalendar().get(Calendar.YEAR));

            try {
                Date voteDate = new SimpleDateFormat("dd/MM/yyy").parse(date);
                Date currentDate = new Date();
                Calendar beforeDate = Calendar.getInstance();

                beforeDate.add(Calendar.DAY_OF_MONTH, -daysFromNow);
                if(voteDate.before(currentDate) && voteDate.after(beforeDate.getTime())){
                    tmp.add(voto);
                }else continue;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }

    public static ArrayList<Voto> getNumericalVotesByMateria(Context c, String materia, int period){
        ArrayList<Voto> tmp = Votes.getVotesByPeriod(c, period);

        ArrayList<Voto> voti = new ArrayList<>();
        for(Voto voto : tmp){
            if(("1234567890".contains(voto.voto.charAt(0) + ""))){
                if(voto.materia.equals(materia)){
                    voti.add(voto);
                }
            }else continue;
        }
        return voti;

    }

    public static double getVoteByString(String value){

            if(value.length()==1){

                if(value.contains("+")) return 12;
                else if(value.contains("-"))return 13;

                if(!("1234567890".contains(value.charAt(0) + ""))) return 11;
                else return Double.parseDouble(value);

            }else if(value.length()==2){
                if(value.endsWith("½")) {
                    return Double.parseDouble(value.substring(0, value.length()-1)) + 0.5;
                }else if(value.endsWith("-")){
                    return Double.parseDouble(value.substring(0, value.length()-1)) - 0.25;
                }else if(value.endsWith("+")){
                    return Double.parseDouble(value.substring(0, value.length()-1)) + 0.25;
                }else if(value.equals("10")){
                    return 10;
                }
            }else if(value.length()==3){
                if(value.endsWith("-")){
                    return Double.parseDouble(value.substring(0, value.length()-1)) - 0.25;
                }else return 11;
            }

            return 0;
    }

    public static double getNumericalVoteByString(String value){

        if(value.length()==1){

            if(value.contains("+")) return 0;
            else if(value.contains("-"))return 0;

            if(!("1234567890".contains(value.charAt(0) + ""))) return 0;
            else return Double.parseDouble(value);

        }else if(value.length()==2){
            if(value.endsWith("½")) {
                return Double.parseDouble(value.substring(0, value.length()-1)) + 0.5;
            }else if(value.endsWith("-")){
                return Double.parseDouble(value.substring(0, value.length()-1)) - 0.25;
            }else if(value.endsWith("+")){
                return Double.parseDouble(value.substring(0, value.length()-1)) + 0.25;
            }else if(value.equals("10")){
                return 10;
            }
        }else if(value.length()==3){
            if(value.endsWith("-")){
                return Double.parseDouble(value.substring(0, value.length()-1)) - 0.25;
            }else return 0;
        }

        return 0;
    }

    public static int getColorByVote(double val){
        if(6<=val && val<=7){
            return Color.parseColor("#ffae00"); //Giallo
        }else{
            if(val>7 && val<11){
                return Color.parseColor("#00FF66"); //Verde
            }else if(val==11) return Color.parseColor("#3F51B5"); //Blu per nav
            else if(val==12) return Color.parseColor("#00FF66"); //Verde per +
            else if(val==13) return Color.parseColor("#FF4343"); //Rosso per -
            else return Color.parseColor("#FF4343"); //Rosso
        }
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

    public static boolean isThereAnyVotes(Context c){
        return SharedPreferences.loadString(c, "datas", "voti") != null ? true : false;
    }

    public static ArrayList<Voto> getVotes(Context c){
        Type type = new TypeToken<List<Voto>>(){}.getType();
        return new Gson().fromJson(SharedPreferences.loadString(c, "datas", "voti"), type);
    }
}
