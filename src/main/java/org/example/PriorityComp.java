package org.example;

import java.util.Comparator;

public class PriorityComp implements Comparator<Cerere> {
    @Override
    public int compare(Cerere stanga, Cerere dreapta) {
        if ( stanga.getPrior() < dreapta.getPrior() )
            return 1;
        if ( stanga.getPrior() > dreapta.getPrior() )
            return -1;

        //altfel trb sa le ordonez dupa data
        return stanga.compare(stanga, dreapta);
    }
}
