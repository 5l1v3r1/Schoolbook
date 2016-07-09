package com.marco.marplex.schoolbook.models;

import java.util.Date;

/**
 * Created by marco on 1/29/16.
 */
public class Evento {
    public String titolo, testo, autore;
    public Date date;
    public Evento(Date data, String titolo, String testo, String autore){
        this.date = data;
        this.titolo = titolo;
        this.testo = testo;
        this.autore = autore;
    }
}
