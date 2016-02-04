package com.example.marplex.schoolbook.models;

/**
 * Created by marco on 1/29/16.
 */
public class Voto {
    public String materia, data, tipo, voto;
    public Voto(String voto,  String materia, String data, String tipo){
        this.voto = voto;
        this.materia = materia;
        this.data = data;
        this.tipo = tipo;
    }
}
