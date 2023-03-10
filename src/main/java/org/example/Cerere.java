package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

public class Cerere implements Comparator<Cerere>{
    private int prior;
    private String text;
    private String data;
    private int resurse;
    private String scop;
    TipCerere tip;
    private Utilizator user;
    private enum TipCerere {
        BULETIN,
        SALARIU,
        CARNET_SOFER,
        CARNET_Elev,
        ACT_CONST,
        AUTORIZATIE,
        CUPON_PENSIE,
        EVENIMENTE;
    }

    public Cerere(){};

    //vf prima data daca am cerere pt evenimente
    public Cerere(String tip, int prior, String data, String txt, Utilizator user) {

        if ( tip.contains("organizare eveniment") == true ) {
            this.resurse = prior;
            this.scop = data;
            this.data = data;
            this.tip = TipCerere.EVENIMENTE;
        } else {
            this.prior = prior;
            this.data = data;
            this.text = txt;
        }

        this.user = user;

        if ( tip.contains("buletin") == true )
            this.tip = TipCerere.BULETIN;

        if ( tip.contains("salarial") == true )
            this.tip = TipCerere.SALARIU;

        if ( tip.contains("sofer") == true )
            this.tip = TipCerere.CARNET_SOFER;

        if ( tip.contains("elev") == true )
            this.tip = TipCerere.CARNET_Elev;

        if ( tip.contains("constitutiv") == true )
            this.tip = TipCerere.ACT_CONST;

        if ( tip.contains("autorizatie") == true )
            this.tip = TipCerere.AUTORIZATIE;

        if ( tip.contains("pensie") == true )
            this.tip = TipCerere.CUPON_PENSIE;

    }

    public int getPrior() {
        return prior;
    }

    public String getData() {
        return data;
    }

    public int getResurse() {
        return resurse;
    }

    public String getScop(){ return scop;}

    @Override
    public int compare(Cerere stanga, Cerere dreapta) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:SS");

        try {
            Date stgdata = format.parse(stanga.data);
            Date drdata = format.parse(dreapta.data);
            Boolean after = stgdata.after(drdata);
            if ( after == true ) {
                return 1;
            }

            Boolean before = stgdata.before(drdata);
            if ( before == true ) {
                return -1;
            }
            return 0;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        String string = data;
        string = string.concat(" - " + text );
        return string;
    }

    public String getNumeUser(){
        return user.nume;
    }

    public Utilizator getUser(){
        return user;
    }

    public void setCoziUser(PriorityQueue<Cerere> asteptareQ, Cerere finalizata) {
        user.setAsteptareQ(asteptareQ);
        user.adaugaLaFinalizateQ(finalizata);
    }

    public void adaugaLaCoadaEvent(Cerere cerere ) {
        user.adaugaLaCoadaEvent(cerere);
    }

}
