DOBRE EMILIA ILIANA - 323 CB 


---
Colectii folosite/complexitati:
- 
- Pentru Managementul Primariei retin toti utilizatorii intr-un arrayList deoarece are fast get mai ales
ca in program nu sterg elementele si nu am nevoie de useri ordonati. Parcurgerea in arrayList cand voi avea nevoie de 
cereri ale unui user reprezinta operatia cu cea mai mare complexitate care este liniara.


- Pentru cererile de solutionat am ales cozile cu prioritate pentru acces rapid, operatiile avand complexitate constanta sau O(logn) pentru inserare
si stergere, pentru cererile finalizate ale unui user am ales cozi fara prioritate
- In cadrul biroului functionarii publici sunt retinuti tot intr-un array list fiind cea mai optima decizie, ca in cazul Managamentului Primariei

---
                                    Clase


-Clasa Utilizator-
-
- Clasa abstracta contine atributele tuturor tipurilor de useri si o metoda abstracta de scriere a unei cererei pe care o face un user, ce urmeaza sa fie implementata pentru 
fiecare tip de utilizator. 
- Metoda intoarce textul cererii sau null aruncand o exceptie CerereException, clasa in care se gaseste mesajul ce va fi scris in fisier la o exceptie
- Toti utilizatorii au 2 cozi de cereri in asteptare si finalizate


-Clasa Cerere-
-

- O cerere are text, prioritate, data, tip si referinta la userul care a trimis-o

- In clasa se implementeaza metoda compare a interfetei Comparator pentru a implementa coada de prioritate a userului, coada cererilor in asteptare ordonate dupa data
- si o metode de actualizare a cozilor userului sau 


-Clasa generica Birou + Functionar-
- 
- Un birou are un arrayList de functionari de acel tip si coada de cereri solutionate de functionari in cadrul acelui birou

- Coada de cereri necesita alt comparator, astfel am creat clasa PriorityComp care implementeaza compare dupa prioritati si apoi data

- In cadrul biroului se cauta functionarul care se cere sa rezolve o cerere si se apeleaza metoda corespunzatoarea a acestuia prin care solutioneaza cererea
  - Functionarul are un nume dupa care este gasit. Clasa generica are in plus calea fisierului unde functionarul noteaza activitatea sa prin metoda solutioneaza
  - Se scrie in fisier in modul append data cererii si userul. Avand cererea cu referinta la userul care a facut-o, se sterge din coada de asteptare cererea 


- In cadrul biroului se retrag cereri dupa data, ceea ce inseamna cautarea in coada de cereri de solutionat cererea de scos si copiat intr-o coada auxiliara celelalte cereri


---
Flow Program
-

- In main citesc linie cu linie din fisier si folosesc clasa ajutatoare Parser pentru lizibilitatea codului. Clasa contine metodele apelate corespunzatoare comenzilor


- Parserul contine primaria cu toate birourile si utilizatorii sai, si in plus alte atribute pentru manipularea fisierelor


- La adaugarea unui user nou se actualizeaza arrayListul de utilizatori


- La o cerere noua se cauta prima data userul care face cererea, dupa ce s-a gasit se incearca scrierea unei cereri prin metoda userului, daca se reuseste se adauga cererea in coada userului dar si intr-un birou


- Schimbari_birou primeste cererea si userul si verifica ce tip de user a facut cererea pentru a pune in biroul corespunzator cererea prin metoda update care adauga cererea in coada dupa cum dicteaza comparatorul


- La retragerea unei cereri se gaseste userul care a facut cererea si se elimina cererea din coada sa de asteptare; pentru birou se incearca retragerea cererii din fiecare birou, metoda intoarce false daca nu s-a retras nimic ceea ce inseamna
ca trebuie sa se caute in alt birou


- Adaugarea unui nou functionar presupune adaugarea acestuia la biroul corespunzator prin verificarea tipului


- Rezolvarea cererii se atribuie sarcina unui functionar identificat prin nume, se identifica biroul apoi se cauta functionarul in birou.
Cand se gaseste se solutioneaza prima cerere din coada de cerereri de solutionat ale biroului respectiv.



---
Bonus
-

- In cadrul primariei exista un nou birou care se ocupa cu tinerea evidentei evenimentelor din oras
- Utilizatorii pot organiza evenimente si trimite cereri catre biroul de organizare, iar acesta atribuie cererea unui functionar
- Functionarul decide daca evenimentul se aproba sau nu in functie de resursele primariei
- Orice utilizator poate face cereri pentru evenimente, astfel userii au in plus o coada de cereri de evenimente si una de evenimente acceptate
- La rezolvarea unei cereri, functionarul ia prima cerere din coada de cereri de solutionat ale biroului de evenimente si o adauga in coada de cereri
respinse sau acceptate ale biroului si apoi ale userului respectiv
- Biroul de evenimente este un caz particular al biroului obisnuit astfel folosesc polimorfism pentru crearea unei clase birou evenimente


  Input:
1) eveniment nou:
  cerere_noua; tip utilizator; organizare eveniment; scop eveniment; resurse necesare pret

2) functionar din biroul de evenimente:
   adauga_functionar; evenimente; nume

3) evenimente_viitoare
  afiseaza evenimentele acceptate in ordinea bugetului de la cele mai putin costisitoare la cele mai scumpe

4) evenimente_acceptate; nume_user
   afiseaza evenimentele acceptate ale unui user 

5) evenimente_respinse 
  se afiseaza evenimentele respinse 
