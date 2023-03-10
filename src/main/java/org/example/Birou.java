package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Birou <T> {
    protected ArrayList<FunctPublic<T>> functionari;
    protected PriorityQueue<Cerere> solutionateQ;
    public Birou() {
        solutionateQ = new PriorityQueue<Cerere>(new PriorityComp());
        functionari = new ArrayList<>();
    }
    public void update(Cerere cerere, T user) {
        solutionateQ.add(cerere);
    }
    public PriorityQueue<Cerere> getSolutionateQ() {
        return solutionateQ;
    }
    public void setSolutionateQ(PriorityQueue<Cerere> solutionateQ) {
        this.solutionateQ = solutionateQ;
    }

    public void addFunctionar(String nume, String file) {

        FunctPublic<T> functionar = new FunctPublic<>(nume, file);
        functionari.add(functionar);

    }

    //un functionar primeste o cerere
    //prima cerere din coada - aceasta apoi se scoate defintiv din coada + se scoate si din
    //coada de asteptare a utilizatorului
    public void cautaFunctionar(String nume){

        FunctPublic<T> functionar = null;
        for( FunctPublic<T> aux : functionari ) {
            String numeFunc = aux.getNume();
            if ( numeFunc.equals(nume) == true ) {
                functionar = aux;
                break;
            }
        }

        if ( functionar != null ) {
            try {
                functionar.solutioneaza(solutionateQ);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    //retragere cerere - caut cererea in birou si daca o gasesc o sterg
    //caut cererea dupa data
    //exista in birou si am sters -> return 0; altfel 1
    public boolean retrage_cerere(String dataCerere) {
        boolean gasit = false;
        PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new PriorityComp());
        while(!solutionateQ.isEmpty()){
            Cerere element = solutionateQ.poll();
            if ( element.getData().equals(dataCerere) != true ) {
                aux.add(element);
            } else {
                gasit = true;
            }
        }
        solutionateQ = aux;
        return gasit;
    }
}

//in plus are o coada de ev acceptate/respinse
// iar coada de solutionate se pastreaza insa se respecta alta regula de comparare
class BirouEvenimente<T> extends Birou<T> {
    private PriorityQueue<Cerere> acceptateQ;
    private Queue<Cerere> respinseQ;

    public BirouEvenimente() {
        solutionateQ = new PriorityQueue<Cerere>(new EventComp());
        functionari = new ArrayList<>();
        acceptateQ = new PriorityQueue<Cerere>(new EventComp());
        respinseQ = new LinkedList<>();
    }

    public PriorityQueue<Cerere> getAcceptateQ() { return acceptateQ; }
    public void setAcceptateQ(PriorityQueue<Cerere> acceptateQ) {
        this.acceptateQ = acceptateQ;
    }

    public Queue<Cerere> getRespinseQ() { return  respinseQ; }
    public void setRespinseQ(Queue<Cerere> respinseQ) {
        this.respinseQ = respinseQ;
    }

    public void cautaFunctionar(String nume) {

        FunctPublic<T> functionar = null;
        for( FunctPublic<T> aux : functionari ) {
            String numeFunc = aux.getNume();
            if ( numeFunc.equals(nume) == true ) {
                functionar = aux;
                break;
            }
        }

        //functionarul decide cererea
        if ( functionar != null ) {
            Cerere cerere = solutionateQ.poll();
            try {
                functionar.decide(acceptateQ, respinseQ, cerere);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
