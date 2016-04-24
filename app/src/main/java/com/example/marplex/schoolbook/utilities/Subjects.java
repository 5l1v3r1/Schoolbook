package com.example.marplex.schoolbook.utilities;

import android.content.Context;

import com.example.marplex.schoolbook.models.Compare;
import com.example.marplex.schoolbook.models.Materia;
import com.example.marplex.schoolbook.models.Voto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by marco on 4/24/16.
 */
public class Subjects {
    public static ArrayList<Materia> getSubjects(Context context, int period){
        ArrayList<Materia> materieList = new ArrayList<>();

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        final ArrayList<String> materie = new Gson().fromJson(SharedPreferences.loadString(context, "materie", "materie"), type);

        for(String materia : materie){
            ArrayList<Voto> materiaVoti = Votes.getNumericalVotesByMateria(context, materia, period);

            double sum = 0;

            for(Voto voto : materiaVoti) {
                sum += Votes.getNumericalVoteByString(voto.voto);
            }

            double totalAverage = MathUtils.rintRound(sum/materiaVoti.size(), 1);
            Materia mMateria = new Materia(totalAverage, materia);
            materieList.add(mMateria);
        }

        return materieList;
    }

    public static ArrayList<String> getSubjects(Context c){
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return new Gson().fromJson(SharedPreferences.loadString(c, "materie", "materie"), type);
    }

    public static ArrayList<Compare> getComparedSubject(Context context, String sub){

        ArrayList<Compare> mCompareList = new ArrayList<>();

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        final ArrayList<String> materie = new Gson().fromJson(SharedPreferences.loadString(context, "materie", "materie"), type);

        for(String materia : materie){
            if(materia.equals(sub)) {
                ArrayList<Voto> materiaVotiOne = Votes.getNumericalVotesByMateria(context, materia, 1);
                ArrayList<Voto> materiaVotiTwo = Votes.getNumericalVotesByMateria(context, materia, 2);

                double sumOne = 0, sumTwo = 0;

                for (Voto voto : materiaVotiOne) {
                    sumOne += Votes.getNumericalVoteByString(voto.voto);
                }
                for (Voto voto : materiaVotiTwo) {
                    sumTwo += Votes.getNumericalVoteByString(voto.voto);
                }

                double totalAverageOne = MathUtils.rintRound(sumOne / materiaVotiOne.size(), 1);
                double totalAverageTwo = MathUtils.rintRound(sumTwo / materiaVotiTwo.size(), 1);

                Compare compare = new Compare(new Materia(totalAverageOne, materia), new Materia(totalAverageTwo, materia));
                mCompareList.add(compare);
            }else continue;
        }

        return mCompareList;
    }

    public static ArrayList<Compare> getComparedSubjects(Context context){

        ArrayList<Compare> mCompareList = new ArrayList<>();

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        final ArrayList<String> materie = new Gson().fromJson(SharedPreferences.loadString(context, "materie", "materie"), type);

        for(String materia : materie){
            ArrayList<Voto> materiaVotiOne = Votes.getNumericalVotesByMateria(context, materia, 1);
            ArrayList<Voto> materiaVotiTwo = Votes.getNumericalVotesByMateria(context, materia, 2);

            double sumOne = 0, sumTwo = 0;

            for(Voto voto : materiaVotiOne) {
                sumOne += Votes.getNumericalVoteByString(voto.voto);
            }
            for(Voto voto : materiaVotiTwo) {
                sumTwo += Votes.getNumericalVoteByString(voto.voto);
            }

            double totalAverageOne = MathUtils.rintRound(sumOne/materiaVotiOne.size(), 1);
            double totalAverageTwo = MathUtils.rintRound(sumTwo/materiaVotiTwo.size(), 1);

            Compare compare = new Compare(new Materia(totalAverageOne, materia), new Materia(totalAverageTwo, materia));
            mCompareList.add(compare);
        }

        return mCompareList;
    }
}
