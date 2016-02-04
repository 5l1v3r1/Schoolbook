package com.example.marplex.schoolbook.interfaces;

import com.example.marplex.schoolbook.models.Voto;

import java.util.List;

/**
 * Created by marco on 1/30/16.
 */
public interface ClassevivaVoti {
    void onVotiReceive(List<Voto> voto);
}
