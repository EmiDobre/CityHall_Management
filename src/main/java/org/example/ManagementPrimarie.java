package org.example;

import java.io.*;
import java.text.ParseException;
import java.util.*;
public class ManagementPrimarie {

    ArrayList<Utilizator> utilizatori;
    Birou<Persoana> persoanaBirou;
    Birou<Angajat> angajatBirou;
    Birou<Pensionar> pensionarBirou;
    Birou<Elev> elevBirou;
    Birou<EntitateJuridica> entitateBirou;

    //folosesc un birou pentru evenimente, birou de utilizatori
    // intrucat orice utilizator poate trimite cereri pentru evenimente
    BirouEvenimente<Utilizator> evenimente;

    public Utilizator gasesteUtilizator(String numeComplet) {
        Utilizator user = null;
        for ( Utilizator aux : utilizatori) {
            if ( aux.nume.equals(numeComplet) == true ) {
                user = aux;
                break;
            }
        }
        return user;
    }

    public ManagementPrimarie() {
        utilizatori = new ArrayList<Utilizator>();
        pensionarBirou = new Birou<>();
        angajatBirou = new Birou<>();
        persoanaBirou = new Birou<>();
        elevBirou = new Birou<>();
        entitateBirou = new Birou<>();
        evenimente = new BirouEvenimente<>();
    }
    public static void main(String[] args) throws IOException, ParseException {

        String input, output;

        if (args.length < 1) {
            //bonus:
            input = "src/main/resources/bonus-input/01_bonus.txt";
            output = "src/main/resources/bonus-output/01_bonus.txt";

        } else {
            input = "src/main/resources/input/";
            output = "src/main/resources/output/";
            input = input.concat(args[0]);
            output = output.concat(args[0]);
        }

        ManagementPrimarie primarie = new ManagementPrimarie();
        File outputFile = new File(output);
        if( outputFile.exists() == true )
            outputFile.delete();

        FileWriter fWriter = new FileWriter(output,true);
        BufferedWriter bwriter = new BufferedWriter(fWriter);

        File file = new File(input);
        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            StringTokenizer token = new StringTokenizer(data, ";");
            ArrayList<String> info = new ArrayList<String>();
            while (token.hasMoreTokens())
                info.add(token.nextToken());
            Parser actions = new Parser(data,info, bwriter, fWriter, primarie, output);

            if ( data.contains("adauga_utilizator") == true )
                actions.utilizator_nou();

            if ( data.contains("cerere_noua") == true )
                actions.cerere_noua();

            if ( data.contains("afiseaza_cereri_in_asteptare") == true ) {
                actions.afisare_in_asteptare();
            }

            if ( data.contains("retrage_cerere") == true )
                actions.retrage_cerere();

            if ( data.contains("afiseaza_cereri;") == true ) {
                actions.afiseaza_cerere();
            }

            if ( data.contains("adauga_functionar") == true ) {
                actions.functionar_nou();
            }

            if ( data.contains("rezolva_cerere") == true ) {
                actions.rezolva_cerere();
            }

            if ( data.contains("afiseaza_cereri_finalizate") == true ) {
                actions.afiseaza_cereri_finalizate();
            }

            //bonus:
            if ( data.contains("viitoare") == true ) {
                actions.evenimente_viitoare();
            }

            if ( data.contains("respinse") == true ) {
                actions.evenimente_respinse();
            }

            if ( data.contains("acceptate") == true ) {
                actions.evenimente_acceptate();
            }

        }
        reader.close();
        bwriter.close();
        fWriter.close();

    }
}

class Parser {
    private ManagementPrimarie primarie;
    private String data;
    private ArrayList<String> info;
    private String output;
    private BufferedWriter bwriter;
    private FileWriter fWriter;

    public Parser(String data, ArrayList<String> info, BufferedWriter bwriter, FileWriter fWriter,
                  ManagementPrimarie primarie, String output) {
        this.data = data;
        this.info = info;
        this.bwriter = bwriter;
        this.fWriter = fWriter;
        this.primarie = primarie;
        this.output = output;
    }

    public void utilizator_nou() {
        String numeComplet = info.get(2).substring(1);

        if (info.get(1).contains("angajat") == true) {
            String detalii = info.get(3);
            Angajat angajat = new Angajat(numeComplet, detalii);
            primarie.utilizatori.add(angajat);
        }

        if (info.get(1).contains("persoana") == true) {
            Persoana pers = new Persoana(numeComplet);
            primarie.utilizatori.add(pers);
        }

        if (info.get(1).contains("pensionar") == true) {
            Pensionar pensionar = new Pensionar(numeComplet);
            primarie.utilizatori.add(pensionar);
        }

        if (info.get(1).contains("elev") == true) {
            String detalii = info.get(3);
            Elev elev = new Elev(numeComplet, detalii);
            primarie.utilizatori.add(elev);
        }

        //in more info e numele reprezentantului
        if (info.get(1).contains("entitate") == true) {
            String detalii = info.get(3).substring(1);
            EntitateJuridica entitate = new EntitateJuridica(numeComplet, detalii);
            primarie.utilizatori.add(entitate);
        }
    }

    public void cerere_noua() throws IOException {

        String numeComplet = info.get(1).substring(1);
        String tipCerere = info.get(2).substring(1);

        if (tipCerere.equals(new String("organizare eveniment")) == true) {
            String scop = info.get(3).substring(1);
            String resurse = info.get(4).substring(1);
            int suma_resurse = Integer.parseInt(resurse);
            //gasesc utilizatorul in set
            Utilizator userCautat = primarie.gasesteUtilizator(numeComplet);
            if (userCautat == null)
                return;

            Cerere cerere = userCautat.cerereEvent(tipCerere, suma_resurse, scop, bwriter);
            //orcine poate propune evenimente - nu am exceptii
            //adaug cererea in biroul de evenimente
            primarie.evenimente.update(cerere, userCautat);

            return;
        }

        String dataCerere = info.get(3).substring(1);
        String priority = info.get(4).substring(1);
        int prior = Integer.parseInt(priority);

        //gasesc utilizatorul in set
        Utilizator userCautat = primarie.gasesteUtilizator(numeComplet);
        if (userCautat == null)
            return;

        //creare si vf exceptie pt cerere:
        Cerere cerere = userCautat.creeazaCerere(tipCerere, prior, dataCerere, bwriter);

        //adaugare cerere pt biroul respectiv
        if (cerere != null)
            schimbari_birou(cerere, userCautat);
    }

    public void schimbari_birou(Cerere cerere, Utilizator user) {

        if (user instanceof Angajat) {
            Angajat angajat = (Angajat) user;
            primarie.angajatBirou.update(cerere, angajat);
        }

        if (user instanceof Persoana) {
            Persoana persoana = (Persoana) user;
            primarie.persoanaBirou.update(cerere, persoana);
        }

        if (user instanceof Pensionar) {
            Pensionar pensionar = (Pensionar) user;
            primarie.pensionarBirou.update(cerere, pensionar);
        }

        if (user instanceof Elev) {
            Elev elev = (Elev) user;
            primarie.elevBirou.update(cerere, elev);
        }

        if (user instanceof EntitateJuridica) {
            EntitateJuridica entitate = (EntitateJuridica) user;
            primarie.entitateBirou.update(cerere, entitate);
        }
    }

    public void afisare_in_asteptare() throws IOException {
        String numeComplet = info.get(1).substring(1);

        //userul
        Utilizator user = primarie.gasesteUtilizator(numeComplet);
        if (user == null)
            return;

        //scriu in output:
        bwriter.write(numeComplet + " - cereri in asteptare:");
        bwriter.newLine();
        PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new Cerere());

        while (!user.asteptareQ.isEmpty()) {
            Cerere element = user.asteptareQ.poll();
            aux.add(element);
            bwriter.write(element.toString());
            bwriter.newLine();
        }
        user.asteptareQ = aux;

    }

    public void retrage_cerere() {

        String numeComplet = info.get(1).substring(1);
        String dataCerere = info.get(2).substring(1);

        //gasesc userul
        Utilizator user = primarie.gasesteUtilizator(numeComplet);
        if (user == null)
            return;

        //sterg cererea din coada userului
        PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new Cerere());
        while (!user.asteptareQ.isEmpty()) {
            Cerere element = user.asteptareQ.poll();
            if (element.getData().equals(dataCerere) != true) {
                aux.add(element);
            }
        }
        user.asteptareQ = aux;

        //caut cererea in toate birourile si o sterg din coada
        boolean cerere_stearsa = primarie.persoanaBirou.retrage_cerere(dataCerere);
        if (cerere_stearsa == false) {

            cerere_stearsa = primarie.angajatBirou.retrage_cerere(dataCerere);
            if (cerere_stearsa == false) {

                cerere_stearsa = primarie.elevBirou.retrage_cerere(dataCerere);
                if (cerere_stearsa == false) {

                    cerere_stearsa = primarie.pensionarBirou.retrage_cerere(dataCerere);
                    if (cerere_stearsa == false) {
                        primarie.entitateBirou.retrage_cerere(dataCerere);

                    }
                }
            }
        }

    }

    //afisare toate cererile dintr-un birou
    public void afiseaza_cerere() throws IOException {
        String birou_tip = info.get(1).substring(1);

        if (birou_tip.contains("angajat") == true) {
            bwriter.write("angajat - cereri in birou:");
            bwriter.newLine();
            PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new PriorityComp());
            PriorityQueue<Cerere> solutionateQ = primarie.angajatBirou.getSolutionateQ();
            while (!solutionateQ.isEmpty()) {
                Cerere element = solutionateQ.poll();
                aux.add(element);
                bwriter.write(element.getPrior() + " - " + element.toString());
                bwriter.newLine();
            }
            primarie.angajatBirou.setSolutionateQ(aux);
        }

        if (birou_tip.contains("persoana") == true) {
            bwriter.write("persoana - cereri in birou:");
            bwriter.newLine();
            PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new PriorityComp());
            PriorityQueue<Cerere> solutionateQ = primarie.persoanaBirou.getSolutionateQ();
            while (!solutionateQ.isEmpty()) {
                Cerere element = solutionateQ.poll();
                aux.add(element);
                bwriter.write(element.getPrior() + " - " + element.toString());
                bwriter.newLine();
            }
            primarie.persoanaBirou.setSolutionateQ(aux);
        }

        if (birou_tip.contains("elev") == true) {
            bwriter.write("elev - cereri in birou:");
            bwriter.newLine();
            PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new PriorityComp());
            PriorityQueue<Cerere> solutionateQ = primarie.elevBirou.getSolutionateQ();
            while (!solutionateQ.isEmpty()) {
                Cerere element = solutionateQ.poll();
                aux.add(element);
                bwriter.write(element.getPrior() + " - " + element.toString());
                bwriter.newLine();
            }
            primarie.elevBirou.setSolutionateQ(aux);
        }

        if (birou_tip.contains("entitate juridica") == true) {
            bwriter.write("entitate juridica - cereri in birou:");
            bwriter.newLine();
            PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new PriorityComp());
            PriorityQueue<Cerere> solutionateQ = primarie.entitateBirou.getSolutionateQ();
            while (!solutionateQ.isEmpty()) {
                Cerere element = solutionateQ.poll();
                aux.add(element);
                bwriter.write(element.getPrior() + " - " + element.toString());
                bwriter.newLine();
            }
            primarie.entitateBirou.setSolutionateQ(aux);
        }

        if (birou_tip.contains("pensionar") == true) {
            bwriter.write("pensionar - cereri in birou:");
            bwriter.newLine();
            PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new PriorityComp());
            PriorityQueue<Cerere> solutionateQ = primarie.pensionarBirou.getSolutionateQ();
            while (!solutionateQ.isEmpty()) {
                Cerere element = solutionateQ.poll();
                aux.add(element);
                bwriter.write(element.getPrior() + " - " + element.toString());
                bwriter.newLine();
            }
            primarie.pensionarBirou.setSolutionateQ(aux);
        }
    }

    public void functionar_nou() {

        String numeComplet = info.get(2).substring(1);
        String tipFunctionar = info.get(1).substring(1);

        String caleFisier = output.substring(0, output.indexOf('1'));
        caleFisier = caleFisier.concat("functionar_");
        caleFisier = caleFisier.concat(numeComplet + ".txt");

        File outputFile = new File(caleFisier);
        if (outputFile.exists() == true)
            outputFile.delete();

        if (tipFunctionar.contains("angajat") == true) {
            primarie.angajatBirou.addFunctionar(numeComplet, caleFisier);
        }

        if (tipFunctionar.contains("persoana") == true) {
            primarie.persoanaBirou.addFunctionar(numeComplet, caleFisier);
        }

        if (tipFunctionar.contains("elev") == true) {
            primarie.elevBirou.addFunctionar(numeComplet, caleFisier);
        }

        if (tipFunctionar.contains("entitate juridica") == true) {
            primarie.entitateBirou.addFunctionar(numeComplet, caleFisier);
        }

        if (tipFunctionar.contains("pensionar") == true) {
            primarie.pensionarBirou.addFunctionar(numeComplet, caleFisier);
        }

        if (tipFunctionar.contains("evenimente") == true) {
            primarie.evenimente.addFunctionar(numeComplet, caleFisier);
        }

    }

    public void rezolva_cerere() {

        String numeComplet = info.get(2).substring(1);
        String tipFunctionar = info.get(1).substring(1);

        if (tipFunctionar.contains("angajat") == true) {
            primarie.angajatBirou.cautaFunctionar(numeComplet);
        }

        if (tipFunctionar.contains("persoana") == true) {
            primarie.persoanaBirou.cautaFunctionar(numeComplet);
        }

        if (tipFunctionar.contains("elev") == true) {
            primarie.elevBirou.cautaFunctionar(numeComplet);
        }

        if (tipFunctionar.contains("entitate juridica") == true) {
            primarie.entitateBirou.cautaFunctionar(numeComplet);
        }

        if (tipFunctionar.contains("pensionar") == true) {
            primarie.pensionarBirou.cautaFunctionar(numeComplet);
        }

        if (tipFunctionar.contains("evenimente") == true) {
            primarie.evenimente.cautaFunctionar(numeComplet);
        }

    }

    public void afiseaza_cereri_finalizate() throws IOException {

        String numeComplet = info.get(1).substring(1);

        //userul
        Utilizator user = primarie.gasesteUtilizator(numeComplet);
        if (user == null)
            return;

        //scriu in output:
        bwriter.write(numeComplet + " - cereri in finalizate:");
        bwriter.newLine();
        PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new Cerere());

        while (!user.finalizateQ.isEmpty()) {
            Cerere element = user.finalizateQ.poll();
            aux.add(element);
            bwriter.write(element.toString());
            bwriter.newLine();
        }
        user.finalizateQ = aux;
    }

    //bonus:
    public void evenimente_viitoare() throws IOException {
        PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new EventComp());
        PriorityQueue<Cerere> acceptateQ = primarie.evenimente.getAcceptateQ();
        bwriter.write("Evenimente viitoare:");
        bwriter.newLine();
        while (!acceptateQ.isEmpty()) {
            Cerere element = acceptateQ.poll();
            aux.add(element);
            bwriter.write(element.getScop()+ " - " + element.getResurse() + " euro");
            bwriter.newLine();
        }
        bwriter.newLine();
        primarie.evenimente.setAcceptateQ(aux);
    }

    public void evenimente_respinse() throws IOException {
        Queue<Cerere> aux = new LinkedList<>();
        Queue<Cerere> respinseQ = primarie.evenimente.getRespinseQ();
        bwriter.write("Evenimente respinse:");
        bwriter.newLine();
        while (!respinseQ.isEmpty()) {
            Cerere element = respinseQ.poll();
            aux.add(element);
            bwriter.write(element.getScop()+ " - " + element.getResurse() + " euro");
            bwriter.newLine();
        }
        bwriter.newLine();
        primarie.evenimente.setRespinseQ(aux);
    }

    public void evenimente_acceptate() throws IOException {
        String numeComplet = info.get(1).substring(1);

        //userul
        Utilizator user = primarie.gasesteUtilizator(numeComplet);
        if (user == null)
            return;

        //scriu in output:
        bwriter.write(numeComplet + " - evenimente acceptate:");
        bwriter.newLine();
        PriorityQueue<Cerere> aux = new PriorityQueue<Cerere>(new EventComp());

        while (!user.ev_acceptateQ.isEmpty()) {
            Cerere element = user.ev_acceptateQ.poll();
            aux.add(element);
            bwriter.write(element.getScop());
            bwriter.newLine();
        }
        bwriter.newLine();
        user.ev_acceptateQ = aux;
    }
}
