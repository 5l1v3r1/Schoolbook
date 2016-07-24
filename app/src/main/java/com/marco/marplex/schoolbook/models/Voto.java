package com.marco.marplex.schoolbook.models;

/**
 * Created by marco on 1/29/16.
 */
public class Voto {
    public String materia, data, tipo, voto;
    public int periodo;
    public boolean special = false;
    public Voto(String voto,  String materia, String data, String tipo, int periodo){
        this.voto = voto;
        this.materia = materia;
        this.data = data;
        this.tipo = tipo;
        this.periodo = periodo;
    }
    public void setSpecial(boolean special){
        this.special = special;
    }

    @Override
    public String toString(){
        return materia + " | " + voto + " | " + data + " | " + periodo;
    }
}
