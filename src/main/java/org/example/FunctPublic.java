package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

public class FunctPublic <T> {
    private String nume;
    private String caleFisier;
    public FunctPublic(String nume, String caleFisier) {
        this.nume = nume;
        this.caleFisier = caleFisier;
    }
    public String getNume() {
        return nume;
    }

    public Cerere solutioneaza(PriorityQueue<Cerere> solutionateQ) throws IOException {

        Cerere cerere = solutionateQ.poll();
        if ( cerere == null )
            return null;

        FileWriter fWriter = new FileWriter(caleFisier,true);
        BufferedWriter bwriter = new BufferedWriter(fWriter);
        bwriter.write(cerere.getData() + " - " + cerere.getNumeUser());
        bwriter.newLine();
        bwriter.close();
        fWriter.close();

        //stergere cerere din coada de asteptare:

        Utilizator user = cerere.getUser();
        PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new Cerere());
        while(!user.asteptareQ.isEmpty()){
            Cerere element = user.asteptareQ.poll();
            if ( element.equals(cerere) == false ) {
                aux.add(element);
            }
        }
        cerere.setCoziUser(aux, cerere);

        return cerere;
    }

    //decizia se ia in functie de resursele primariei
    public void decide(PriorityQueue<Cerere> acceptateQ, Queue<Cerere> respinseQ, Cerere cerere ) throws IOException {

        FileWriter fWriter = new FileWriter(caleFisier,true);
        BufferedWriter bwriter = new BufferedWriter(fWriter);
        if ( cerere.getResurse() >= 7800 ) {
            bwriter.write("Cerere respinsa: " + cerere.getScop());
            bwriter.newLine();
            respinseQ.add(cerere);
        } else {
            bwriter.write("Cerere aprobata: " + cerere.getScop());
            bwriter.newLine();
            acceptateQ.add(cerere);
            //actualizeaza coada de evenimente acceptate a userului:
            cerere.adaugaLaCoadaEvent(cerere);
        }

        bwriter.close();
        fWriter.close();

    }

}
