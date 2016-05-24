package com.marco.marplex.schoolbook.reminds;

import android.content.Intent;
import android.os.Bundle;

import com.marco.marplex.schoolbook.DashboardActivity;
import com.marco.marplex.schoolbook.Materia;
import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.fragments.Agenda;
import com.marco.marplex.schoolbook.fragments.Dashboard;
import com.marco.marplex.schoolbook.fragments.Voti;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.fragments.tabs.Reminds;
import com.marco.marplex.schoolbook.utilities.TTS;

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
        if(contains("voti") || contains("fammi vedere i miei voti") || contains("fammi vedere i voti") || contains("miei voti")){
            switchFragment(new Voti(), R.id.voti, R.color.colorPrimaryGreen, R.color.colorPrimaryDarkGreen);
            new TTS(fragment.getContext())
                    .talk("Ecco la lista dei tuoi voti");
            return true;
        }else if(startWith("che media ho in ") || startWith("che medio in ") || startWith("media in") || startWith("media ")){
            //Get the materia text
            String rawMateria = text.substring(text.lastIndexOf(" ")+1, text.length());
            String materia = rawMateria.substring(0, 1).toUpperCase() + rawMateria.substring(1);

            //Create the bundle
            Bundle b = new Bundle();
            b.putString("materia", materia);
            b.putInt("periodo", 2);

            //Start the activity
            Intent i = new Intent(fragment.getActivity(), Materia.class);
            i.putExtras(b);
            fragment.startActivity(i);
            switchFragment(new Dashboard(), R.id.dashboard, R.color.colorPrimary, R.color.colorPrimaryDark);
            return true;
        }else if(startWith("obiettivo ")){
            //Get the materia text
            String rawMateria = text.substring(text.indexOf(" ")+1, text.lastIndexOf(" "));
            String materia = rawMateria.substring(0, 1).toUpperCase() + rawMateria.substring(1);

            try {
                int obbiettivo = Integer.parseInt(text.substring(text.lastIndexOf(" ") + 1, text.length()));

                //Create the bundle
                Bundle b = new Bundle();
                b.putString("materia", materia);
                b.putInt("periodo", 2);
                b.putInt("obiettivo", obbiettivo);

                //Start the activity
                Intent i = new Intent(fragment.getActivity(), Materia.class);
                i.putExtras(b);
                fragment.startActivity(i);
                switchFragment(new Dashboard(), R.id.dashboard, R.color.colorPrimary, R.color.colorPrimaryDark);
            }catch (Exception e){ }
            return true;
        }else if(contains("eventi di oggi") || contains("eventi oggi") || contains("notifiche di oggi") || contains("eventi per oggi")){
            Agenda agenda = new Agenda();
            Bundle bundle = new Bundle();
            bundle.putBoolean("now", true);
            agenda.setArguments(bundle);
            switchFragment(agenda, R.id.agenda, R.color.colorPrimaryTeal, R.color.colorPrimaryDarkTeal);
            return true;
        }else return false;
    }

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

}
