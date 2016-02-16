package com.example.marplex.schoolbook.interfaces;

import com.example.marplex.schoolbook.models.Evento;

import java.util.ArrayList;

/**
 * Created by marco on 1/30/16.
 */
public interface ClassevivaAgenda {
    void onAgendaReceive(ArrayList<Evento> agenda);
}
