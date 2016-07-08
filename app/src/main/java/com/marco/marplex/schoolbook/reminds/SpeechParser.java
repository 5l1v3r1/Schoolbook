package com.marco.marplex.schoolbook.reminds;

import android.content.Intent;
import android.os.Bundle;

import com.marco.marplex.schoolbook.DashboardActivity;
import com.marco.marplex.schoolbook.Materia;
import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.VoteDialogActivity;
import com.marco.marplex.schoolbook.fragments.Agenda;
import com.marco.marplex.schoolbook.fragments.Dashboard;
import com.marco.marplex.schoolbook.fragments.Voti;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.fragments.tabs.Reminds;
import com.marco.marplex.schoolbook.utilities.TTS;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

/**
 * Created by marco on 5/22/16.
 */

public class SpeechParser {
    private String text;
    private Reminds fragment;
    private ArrayList datas;

    public SpeechParser(String text, ArrayList datas, Reminds fragment){
        this.text = text;
        this.fragment = fragment;
        this.datas = datas;
    }

    public boolean parseAndGo(){

        //--------------------------------------- OPEN VOTES ---------------------------------------
        if(contains("voti") || contains("fammi vedere i miei voti") || contains("fammi vedere i voti") || contains("miei voti")){
            switchFragment(new Voti(), R.id.voti, R.color.colorPrimaryGreen, R.color.colorPrimaryDarkGreen);
            new TTS(fragment.getContext())
                    .talk("Ecco la lista dei tuoi voti");
            return true;

        //--------------------------------------- SEE THE AVERAGE ---------------------------------------
        }else if(startWith("che media ho in ") || startWith("che medio in ") || startWith("media in") || startWith("media ")){
            //Get the materia text
            String rawMateria = text.substring(text.lastIndexOf(" ")+1, text.length());
            String materia = rawMateria.substring(0, 1).toUpperCase() + rawMateria.substring(1);

            //Create the bundle
            Bundle b = new Bundle();

            b.putString("materia", materia);

            //Find the current period
            if(Votes.getVotesByPeriod(fragment.getContext(), 2) == null || Votes.getVotesByPeriod(fragment.getContext(), 2).size() == 0){
                b.putInt("periodo", 1);
            }else b.putInt("periodo", 2);

            //Start the activity
            Intent i = new Intent(fragment.getActivity(), Materia.class);
            i.putExtras(b);
            fragment.startActivity(i);
            switchFragment(new Dashboard(), R.id.dashboard, R.color.colorPrimary, R.color.colorPrimaryDark);
            return true;
        //--------------------------------------- SET AN OBJECTIVE ---------------------------------------
        }else if(startWith("obiettivo ")){
            //Get the materia text
            String rawMateria = text.substring(text.indexOf(" ")+1, text.lastIndexOf(" "));
            String materia = rawMateria.substring(0, 1).toUpperCase() + rawMateria.substring(1);

            try {
                int obbiettivo = Integer.parseInt( (text.substring(text.lastIndexOf(" ") + 1, text.length())) == "X" ? "10" : text.substring(text.lastIndexOf(" ") + 1, text.length()));

                //Create the bundle
                Bundle b = new Bundle();
                b.putString("materia", materia);

                //Find the current period
                if(Votes.getVotesByPeriod(fragment.getContext(), 2) == null || Votes.getVotesByPeriod(fragment.getContext(), 2).size() == 0){
                    b.putInt("periodo", 1);
                }else b.putInt("periodo", 2);

                b.putInt("obiettivo", obbiettivo);

                //Start the activity
                Intent i = new Intent(fragment.getActivity(), Materia.class);
                i.putExtras(b);
                fragment.startActivity(i);
                switchFragment(new Dashboard(), R.id.dashboard, R.color.colorPrimary, R.color.colorPrimaryDark);
            }catch (Exception e){ }
            return true;
        //--------------------------------------- SEE TODAY'S EVENTS ---------------------------------------
        }else if(contains("eventi di oggi") || contains("eventi oggi") || contains("notifiche di oggi") || contains("eventi per oggi")){
            Agenda agenda = new Agenda();
            Bundle bundle = new Bundle();
            bundle.putBoolean("now", true);
            agenda.setArguments(bundle);
            switchFragment(agenda, R.id.agenda, R.color.colorPrimaryTeal, R.color.colorPrimaryDarkTeal);
            return true;
            //--------------------------------------- HOW MUCH 10 DO I HAVE? ---------------------------------------
        }else if(startWith("quanti ") && ( endsWith(" ho") || endsWith(" ho?") || endsWith(" o?") || endsWith(" o")) ){

            try {
                //Get the number
                final int vote = Integer.parseInt(text.substring(text.indexOf(" ") + 1, text.lastIndexOf(" ")));

                //Start the activity with extra
                Intent i = new Intent(fragment.getContext(), VoteDialogActivity.class);
                i.putExtra("vote", vote);

                fragment.startActivity(i);
                switchFragment(new Dashboard(), R.id.dashboard, R.color.colorPrimary, R.color.colorPrimaryDark);
            }catch (Exception e){ e.printStackTrace(); }

            return true;
        }else return false;
    }



    //--------------------------------------- USEFUL METHODS ---------------------------------------

    private void switchFragment(DrawerFragment switchedFragment, int res, int colorPrimary, int colorPrimaryDark){
        ((DashboardActivity) fragment.getActivity()).setContainerFragment(switchedFragment);
        ((DashboardActivity) fragment.getActivity()).changeActivityColor(colorPrimary, colorPrimaryDark);
        ((DashboardActivity) fragment.getActivity()).navigationView.setCheckedItem(res);
    }

    private boolean contains(String string){
        String tmp = text.toLowerCase();
        return tmp.contains(string.toLowerCase());
    }

    private boolean startWith(String string){
        String tmp = text.toLowerCase();
        return tmp.startsWith(string.toLowerCase());
    }

    private boolean endsWith(String string){
        String tmp = text.toLowerCase();
        return tmp.endsWith(string.toLowerCase());
    }

}
