package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class Utilizator {
    protected String nume;
    protected String companie;
    protected String scoala;
    protected String companie_repr;
    protected PriorityQueue<Cerere> asteptareQ;

    protected PriorityQueue<Cerere> evenimenteQ;
    protected PriorityQueue<Cerere> ev_acceptateQ;
    protected Queue<Cerere> finalizateQ;
    public void setAsteptareQ(PriorityQueue<Cerere> asteptareQ) {
        this.asteptareQ = asteptareQ;
    }
    public void adaugaLaFinalizateQ(Cerere cerere_finalizata) {
        finalizateQ.add(cerere_finalizata);
    }
    public abstract String scrieCerere(String tip) throws CerereException;

    public Cerere creeazaCerere(String tip, int prior, String data, BufferedWriter bwriter) throws IOException {

        try {
            String txt = this.scrieCerere(tip);
            Cerere cerere = new Cerere(tip, prior, data, txt, this);
            asteptareQ.add(cerere);
            return cerere;
        }
        catch (CerereException e ) {
            bwriter.write(e.message);
            bwriter.newLine();
            return null;
        }

    }

    public Cerere cerereEvent(String tip, int resurse, String scop, BufferedWriter bwriter ) {
        Cerere cerere = new Cerere(tip, resurse, scop, null, this);

        evenimenteQ.add(cerere);
        return cerere;
    }

    public void adaugaLaCoadaEvent( Cerere cerere ) {
        ev_acceptateQ.add(cerere);
    }

}

class Persoana extends Utilizator {

    Persoana( String nume ) {
        this.nume = nume;
        asteptareQ = new PriorityQueue<Cerere>(new Cerere());
        finalizateQ = new LinkedList<>();
        evenimenteQ = new PriorityQueue<Cerere>(new EventComp());
        ev_acceptateQ = new PriorityQueue<Cerere>(new EventComp());
    }

    public String scrieCerere(String tip) throws CerereException {

        String cerereTxt = new String("Subsemnatul ");
        cerereTxt = cerereTxt.concat(nume + ", va rog sa-mi aprobati " +
                "urmatoarea solicitare: ");

        if ( tip.contains("buletin") == true ) {
            cerereTxt = cerereTxt.concat("inlocuire buletin");
        } else {

            if (tip.contains("sofer") == true ) {
                cerereTxt = cerereTxt.concat("inlocuire carnet de sofer");
            } else
                //exceptie
                cerereTxt = null;
        }

        if ( cerereTxt == null ) {
            String mesaj = new String("Utilizatorul de tip persoana nu poate " +
                    "inainta o cerere de tip " + tip);
            throw new CerereException(mesaj);
        }

        return cerereTxt;
    }
}

class Angajat extends Utilizator {

    Angajat(String nume, String companie) {
        this.nume = nume;
        this.companie = companie;
        asteptareQ = new PriorityQueue<Cerere>(new Cerere());
        finalizateQ = new LinkedList<>();
        evenimenteQ = new PriorityQueue<Cerere>(new EventComp());
        ev_acceptateQ = new PriorityQueue<Cerere>(new EventComp());
    }

    public String scrieCerere(String tip) throws CerereException {
        String cerereTxt = new String("Subsemnatul ");
        cerereTxt = cerereTxt.concat(nume + ", angajat " +
                "la compania" + companie + ", va rog sa-mi aprobati " +
                "urmatoarea solicitare: ");

        boolean ok = false;

        if ( tip.contains("buletin") == true ) {
            cerereTxt = cerereTxt.concat("inlocuire buletin");
            ok = true;
        }

        if (tip.contains("sofer") == true ) {
            cerereTxt = cerereTxt.concat("inlocuire carnet de sofer");
            ok = true;
        }

        if ( tip.contains("salarial") == true ) {
            cerereTxt = cerereTxt.concat("inregistrare venit salarial");
            ok = true;
        }

        //exceptie
        if ( ok == false )
            cerereTxt = null;

        if ( cerereTxt == null ) {
            String mesaj = new String("Utilizatorul de tip angajat nu poate " +
                    "inainta o cerere de tip " + tip);
            throw new CerereException(mesaj);
        }

        return cerereTxt;
    }
}

class Pensionar extends Utilizator {
    Pensionar(String nume) {
        this.nume = nume;
        asteptareQ = new PriorityQueue<Cerere>(new Cerere());
        finalizateQ = new LinkedList<>();
        evenimenteQ = new PriorityQueue<Cerere>(new EventComp());
        ev_acceptateQ = new PriorityQueue<Cerere>(new EventComp());
    }

    public String scrieCerere(String tip) throws CerereException{
        String cerereTxt = new String("Subsemnatul ");
        cerereTxt = cerereTxt.concat(nume + ", va rog sa-mi aprobati " +
                "urmatoarea solicitare: ");

        boolean ok = false;

        if ( tip.contains("buletin") == true ) {
            cerereTxt = cerereTxt.concat("inlocuire buletin");
            ok = true;
        }

        if (tip.contains("sofer") == true ) {
            cerereTxt = cerereTxt.concat("inlocuire carnet de sofer");
            ok = true;
        }

        if ( tip.contains("pensie") == true ) {
            cerereTxt = cerereTxt.concat("inregistrare cupoane de pensie");
            ok = true;
        }

        //exceptie
        if ( ok == false )
            cerereTxt = null;

        if ( cerereTxt == null ) {
            String mesaj = new String("Utilizatorul de tip pensionar nu poate " +
                    "inainta o cerere de tip " + tip);
            throw new CerereException(mesaj);
        }


        return cerereTxt;
    }
}

class Elev extends Utilizator {
    Elev(String nume, String scoala){
        this.nume = nume;
        this.scoala = scoala;
        asteptareQ = new PriorityQueue<Cerere>(new Cerere());
        finalizateQ = new LinkedList<>();
        evenimenteQ = new PriorityQueue<Cerere>(new EventComp());
        ev_acceptateQ = new PriorityQueue<Cerere>(new EventComp());
    }

    public String scrieCerere(String tip) throws CerereException{
        String cerereTxt = new String("Subsemnatul ");
        cerereTxt = cerereTxt.concat(nume + ", elev la scoala" + scoala
                + ", va rog sa-mi aprobati " + "urmatoarea solicitare: ");

        boolean ok = false;

        if ( tip.contains("buletin") == true ) {
            cerereTxt = cerereTxt.concat("inlocuire buletin");
            ok = true;
        }

        if (tip.contains("elev") == true ) {
            cerereTxt = cerereTxt.concat("inlocuire carnet de elev");
            ok = true;
        }

        //exceptie
        if ( ok == false )
            cerereTxt = null;

        if ( cerereTxt == null ) {
            String mesaj = new String("Utilizatorul de tip elev nu poate " +
                    "inainta o cerere de tip " + tip);
            throw new CerereException(mesaj);
        }


        return cerereTxt;
    }

}

class EntitateJuridica extends Utilizator {

    EntitateJuridica(String numeComp, String numePers) {
        this.companie_repr = numePers;
        this.nume = numeComp;
        asteptareQ = new PriorityQueue<Cerere>(new Cerere());
        finalizateQ = new LinkedList<>();
        evenimenteQ = new PriorityQueue<Cerere>(new EventComp());
        ev_acceptateQ = new PriorityQueue<Cerere>(new EventComp());
    }

    public String scrieCerere(String tip) throws CerereException {
        String cerereTxt = new String("Subsemnatul ");
        cerereTxt = cerereTxt.concat(companie_repr + ", reprezentant legal al companiei " +
                nume + ", va rog sa-mi aprobati urmatoarea solicitare: ");

        boolean ok = false;

        if (tip.contains("constitutiv") == true ) {
            cerereTxt = cerereTxt.concat("creare act constitutiv");
            ok = true;
        }

        if (tip.contains("autorizatie") == true ) {
            cerereTxt = cerereTxt.concat("reinnoire autorizatie");
            ok = true;
        }

        //exceptie
        if (ok == false)
            cerereTxt = null;

        if ( cerereTxt == null ) {
            String mesaj = new String("Utilizatorul de tip entitate juridica nu poate " +
                    "inainta o cerere de tip " + tip);
            throw new CerereException(mesaj);
        }

        return cerereTxt;
    }

}
