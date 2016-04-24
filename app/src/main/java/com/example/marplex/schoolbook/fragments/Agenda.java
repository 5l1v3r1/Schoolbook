package com.example.marplex.schoolbook.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.connections.ClassevivaAPI;
import com.example.marplex.schoolbook.fragments.custom.CustomCaldroid;
import com.example.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.example.marplex.schoolbook.interfaces.ClassevivaAgenda;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.models.Evento;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Agenda extends DrawerFragment implements ClassevivaAgenda,classeViva {
    CustomCaldroid caldroidFragment;
    ArrayList<Evento> eventi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        /**
         * @see DrawerFragment
         */
        setTabGone();
        removeMenuItems();

        caldroidFragment = new CustomCaldroid();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, final View view) {

                final View dialogView = View.inflate(getActivity(), R.layout.agenda_dialog_custom, null);

                TextView prof = (TextView) dialogView.findViewById(R.id.prof);
                TextView testo = (TextView) dialogView.findViewById(R.id.text);

                Evento evento = null;
                for(int i=0; i< eventi.size(); i++){
                    Evento evento1 = eventi.get(i);
                    String data = formatter.format(date);
                    StringTokenizer tokenizerData = new StringTokenizer(data, "/");
                    String day = tokenizerData.nextToken();
                    String month = tokenizerData.nextToken();
                    String year = tokenizerData.nextToken();

                    StringTokenizer tokenizer = new StringTokenizer(evento1.data, "-");
                    String anno = tokenizer.nextToken();
                    String mese = tokenizer.nextToken();
                    String giorno = tokenizer.nextToken();
                    System.out.println("Day: "+day);
                    System.out.println("Month: "+month);
                    System.out.println("Year: "+year);
                    if(day.equals(giorno) && month.equals(mese) && year.equals(anno)){
                        evento = evento1;
                    }
                }if(evento==null) return;

                prof.setText(evento.titolo);
                testo.setText(evento.testo);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);

                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        revealShow(dialogView, view, true, null);
                    }
                });
                dialogView.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        revealShow(dialogView, view, false, dialog);
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }

            @Override
            public void onChangeMonth(int month, int year) {

            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                Button leftButton = caldroidFragment.getLeftArrowButton();

                Drawable left = getResources().getDrawable( R.mipmap.action_previous );
                ColorFilter filter = new LightingColorFilter( Color.WHITE, Color.BLACK);
                left.setColorFilter(filter);

                leftButton.setBackground(left);
                Button rightButton = caldroidFragment.getRightArrowButton();

                Drawable right = getResources().getDrawable( R.mipmap.action_next );
                ColorFilter filter2 = new LightingColorFilter( Color.WHITE, Color.BLACK);
                right.setColorFilter(filter2);

                rightButton.setBackground(right);
                TextView textView = caldroidFragment.getMonthTitleTextView();
            }

        };

        caldroidFragment.setCaldroidListener(listener);

        ClassevivaAPI api = new ClassevivaAPI(this);
        api.getAgenda(this, getActivity());

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.frameLayout, caldroidFragment);
        t.commit();

        return rootView;
    }

    public static Drawable convertColorDrawable(int resource, int color, Context context) {
        final Drawable drawable = context.getResources().getDrawable(resource);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable.mutate();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealShow(View rootView,View viewDialog, boolean reveal, final AlertDialog dialog) {
        final View view = rootView.findViewById(R.id.reveal_view);
        final Button save = (Button) rootView.findViewById(R.id.btn_save);
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4) * (float)1.2;

        if(reveal){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                    (int) viewDialog.getX(), (int) viewDialog.getY(), 0, maxRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, w/2, h-(save.getHeight()/2), maxRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });

            anim.start();
        }
    }


    @Override
    public void onAgendaReceive(ArrayList<Evento> agenda) {
        eventi = agenda;
        HashMap<String, Object> extraData = caldroidFragment.getExtraData();
        extraData.put("eventi", agenda);
        caldroidFragment.refreshView();
    }

    @Override
    public void onPageLoaded(String html) {

    }

    @Override
    public String getTitle() {
        return "Agenda";
    }
}
