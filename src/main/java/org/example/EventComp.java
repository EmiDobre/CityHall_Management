package org.example;

import java.util.Comparator;

public class EventComp implements Comparator<Cerere> {
    @Override
    public int compare(Cerere stanga, Cerere dreapta) {
        if ( stanga.getResurse() > dreapta.getResurse() )
            return 1;
        if ( stanga.getResurse() < dreapta.getResurse() )
            return -1;
        return 0;
    }
}
