package com.example.marplex.schoolbook.fragments;


import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ListView;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.EventAdapter;
import com.example.marplex.schoolbook.connections.ClassevivaCaller;
import com.example.marplex.schoolbook.decorator.CalendarDecorator;
import com.example.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.example.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.example.marplex.schoolbook.models.Evento;
import com.example.marplex.schoolbook.utilities.Events;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Agenda extends DrawerFragment implements ClassevivaCallback<Evento>{

    @Bind(R.id.calendarView) MaterialCalendarView mCalendar;

    ArrayList<Evento> mEvents;
    ClassevivaCaller mCaller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View rootView = View.inflate(getContext(),R.layout.fragment_agenda, null );
        ButterKnife.bind(this, rootView);

        setTabGone();
        removeMenuItems();

        if(Events.isThereAnyEvents(getContext())){
            populateCalendar(Events.getSavedEvents(getContext()));
        }else{
            mCaller = new ClassevivaCaller(this, getContext());
            mCaller.getAgenda();
        }

        mCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull final MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if(Events.getEventsByDate(mEvents, date.getDate()).size() == 0){}
                else {
                    final View view = View.inflate(getContext(), R.layout.agenda_dialog_custom, null);

                    ListView list = (ListView) view.findViewById(R.id.list_events);
                    Button okButton = (Button) view.findViewById(R.id.btn_save);
                    final CardView card = (CardView) view.findViewById(R.id.reveal_view);

                    final AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setView(view)
                            .create();

                    list.setAdapter(new EventAdapter(getContext(), R.layout.model_event, Events.getEventsByDate(mEvents, date.getDate())));

                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int cx = view.getRight() - (view.getRight() - (view.getWidth() / 2));
                            int cy = view.getBottom() - (view.getBottom() - (view.getHeight() / 2));

                            // get the final radius for the clipping circle
                            int dx = Math.max(cx, card.getWidth() - cx);
                            int dy = Math.max(cy, card.getHeight() - cy);
                            float finalRadius = (float) Math.hypot(dx, dy);

                            SupportAnimator animator =
                                    ViewAnimationUtils.createCircularReveal(card, cx, cy, 0, finalRadius);
                            animator.setInterpolator(new AccelerateDecelerateInterpolator());
                            animator.setDuration(500);
                            animator.start();
                        }
                    });

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View myView) {
                            int cx = view.getRight() - (view.getRight() - (view.getWidth() / 2));
                            int cy = view.getBottom() - (view.getBottom() - (view.getHeight() / 2));

                            // get the final radius for the clipping circle
                            int dx = Math.max(cx, card.getWidth() - cx);
                            int dy = Math.max(cy, card.getHeight() - cy);
                            float finalRadius = (float) Math.hypot(dx, dy);

                            SupportAnimator animator =
                                    ViewAnimationUtils.createCircularReveal(card, cx, cy, 0, finalRadius);
                            animator.setInterpolator(new AccelerateDecelerateInterpolator());
                            animator.setDuration(1000);
                            animator = animator.reverse();
                            animator.addListener(new SupportAnimator.AnimatorListener() {
                                @Override
                                public void onAnimationEnd() {
                                    dialog.cancel();
                                }

                                @Override
                                public void onAnimationCancel() {
                                }

                                @Override
                                public void onAnimationRepeat() {
                                }

                                @Override
                                public void onAnimationStart() {
                                }
                            });
                            animator.start();
                        }
                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public String getTitle() {
        return "Agenda";
    }

    @Override
    public void onResponse(final ArrayList<Evento> list) {
        populateCalendar(list);
    }

    private void populateCalendar(final ArrayList<Evento> events){
        mEvents = events;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collection<CalendarDay> collection = new ArrayList<>();
                for (Evento event: events){
                    collection.add(new CalendarDay(event.date));
                }
                mCalendar.addDecorator(new CalendarDecorator(ContextCompat.getColor(getContext(), R.color.colorPrimaryTeal), collection));
            }
        });
    }
}
