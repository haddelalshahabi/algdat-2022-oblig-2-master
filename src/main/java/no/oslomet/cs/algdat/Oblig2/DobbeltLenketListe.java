package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.*;


public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     *
     * @param <T>
     */

    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    //instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
        hode = null;
        hale = null;
        antall = 0;
        endringer = 0;
    }

    //Sjekkliste for konstruktøren DobbeltLenketListe(T[])
    public DobbeltLenketListe(T[] a) {
        if (a == null) {// sjekk a
            throw new NullPointerException("Tabellen er en null!!");
        }

        Node<T> tidligereNode  = null;
        Node<T> nyNode = null;

        for (T verdi : a){
            if (verdi == null)
                continue;
            antall ++;
            nyNode = new Node<>(verdi, tidligereNode, null);

            if (tidligereNode != null){
                tidligereNode.neste = nyNode;
            } else {
                hode = nyNode;
            }
            tidligereNode = nyNode;
        }
        //endering
        hale = nyNode;
    }

    //Lager metoden fratilKontroll
    public static void fratilKontroll(int antall, int fra, int til){
        if (fra < 0){
            throw new IndexOutOfBoundsException("Fra (" + fra + ") er negativ!");
        }

        if (til > antall){
            throw new IndexOutOfBoundsException("Til (" + til + ") > antall (" + antall + ")");
        }

        if (fra > til){
            throw new IllegalArgumentException("Fra (" + fra + ") > til (" + til + ") - illegalt intervall" );
        }
    }
    //Oppgave 3 b)
    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(antall, fra, til);
        DobbeltLenketListe<T> plist = new DobbeltLenketListe<>();
        for (int i = fra; i < til; i++){
            plist.leggInn(finnNode(i).verdi);
        }
        return plist;
    }
    


    @Override
    //Oppgave 1
    public int antall() {
        //retunerer antall verdier
        return antall;
    }

    @Override
    //Oppgave 1
    public boolean tom() {
        //returnere true/false avhengig av om listen er tom eller ikke
        return antall == 0;
    }

    @Override
    //oppgave 2 b)
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ikke lov med null-verdier!");
        if (antall == 0){
            hode = new Node<>(verdi, null, null);
            hale = hode;
        }else{
            hale.neste = new Node<>(verdi,hale, null);
            hale = hale.neste;
        }
        antall ++;
        endringer ++;
        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "ikke ta null verdier");
        if (indeks == 0 && antall == 0){
            hode = new Node<>(verdi, null, null);
            hale = hode;
            hode.neste = null;
            hode.forrige = null;
        } else if (indeks == 0 && antall > 0){
            hode = finnNode(0);
            Node<T> nåværende = hode;
            Node<T> rNode = new Node<>(verdi, null, nåværende);
            rNode.neste = rNode;
            nåværende.forrige = rNode;
            hode = rNode;
        } else if (indeks == antall){
            Node<T> rNode = new Node<>(verdi, hale, null);
            if (hale != null){
                rNode.forrige = hale;
                hale.neste = rNode;
                hale = rNode;
            }
        } else {
            Node<T> før = null;
            Node<T> nåværende = hode;
            for (int i = 1; i < indeks; i++)
                nåværende = nåværende.neste;
            før = nåværende;
            nåværende = nåværende.neste;
            Node<T> rNode = new Node<>(verdi, før, nåværende);
            før.neste = rNode;
            rNode.neste = nåværende;
            nåværende.forrige = rNode;
            rNode.forrige = før;
        }
        antall++;
        endringer++;

    }

    @Override
    public boolean inneholder(T verdi) {
        if (indeksTil(verdi) != -1)
            return true;
        else return false;
    }

    //Hjelpemetoden for oppgave 3 a)
    private Node<T> finnNode(int indeks){
        Node<T> nåværende = null;
        if (indeks == 0 && antall == 1){
            nåværende = hode;
            return nåværende;
        }

        if (indeks == antall-1){
            return hale;
        }

        if (indeks <=  (int) antall/2){
            nåværende = hode;
            for (int i=0; i < indeks; i++){
                nåværende = nåværende.neste;
            }
        }else {
            nåværende = hale;
            for (int j= antall-1; j > indeks; j--){
                nåværende = nåværende.forrige;
            }
        }
        return nåværende;
    }

    //Oppgave 3 a)
    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        int posisjonIndex = -1;
        for (int i = 0; i < antall; i++) {
            if (hent(i).equals(verdi)) {
                posisjonIndex = i;
                break;
            }
        }
        return posisjonIndex;
    }

    //Oppgave 3a
    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi, "Ikke lov med null-verdier!");
        indeksKontroll(indeks, false);
        Node<T> v = finnNode(indeks);
        T forrigeVerdi = v.verdi;
        v.verdi = nyverdi;
        endringer ++;
        return forrigeVerdi;
    }

    @Override
    //Oppgave 6 del1
    public boolean fjern(T verdi) {
        if (verdi == null) {
            return false;
        }
            Node<T> slett = null;
            Node<T> Node = hode;
            while (Node != null) {
                if (Node.verdi.equals(verdi)) {
                    slett = Node;
                    break;
                } else {
                    Node = Node.neste;
                }
            }
            if (slett == null) {
                return false;
            }
                slett.verdi = null;
                if (slett.forrige != null) {
                    slett.forrige.neste = slett.neste;
                }

                if (slett.forrige != null) {
                    slett.neste.forrige = slett.forrige;
                }

                if (slett == hode && slett.neste != null) {
                    hode = slett.neste;
                }

                if (slett == hale && slett.forrige != null) {
                    hale = slett.forrige;
                }

                antall--;
                endringer++;
                return true;


            }

    @Override
    //oppgave 6 del2
    public T fjern(int indeks) {
        indeksKontroll(indeks, false);
        if (antall == 0) {
            throw new NoSuchElementException("Liste er tomt");

        }
            Node<T> slett = finnNode(indeks);

        if (slett == null) {
                return null;
        }

        if (slett.forrige != null) {
            slett.forrige.neste = slett.neste;
        }

            if (slett.neste != null) {
                slett.neste.forrige = slett.forrige;
            }

            if (slett == hode && slett.neste != null) {
                hode = slett.neste;
            }

            if (slett == hale && slett.forrige != null) {
                hale = slett.forrige;
            }

            antall--;
            endringer++;
            return slett.verdi;
    }



    @Override
    //oppgave 7
    public void nullstill() {
        Node<T> v = hode, q = hale;
        while (v != null){
            v= q.neste = null;
            q.verdi = null;
            q=v;
        }

        hode= hale = null;
        antall = 0;
        endringer++;
        //throw new UnsupportedOperationException();
    }

    //Oppgave 2 a)
    @Override
    public String toString() {
        StringBuilder fremover = new StringBuilder();
        if (tom()){
            return "[]";
        }

        fremover.append('[');
        Node<T> v = hode;

        fremover.append(v.verdi);
        v = v.neste;

        while (v != null){
            fremover.append(',').append(' ').append(v.verdi);
            v = v.neste;
        }

        fremover.append(']');
        return fremover.toString();
    }

    //Oppgave 2 a)
    public String omvendtString() {
        StringBuilder bakover = new StringBuilder();
        if (tom()){
            return "[]";
        }

        bakover.append('[');
        Node<T> v = hale;

        bakover.append(v.verdi);
        v = v.forrige;

        while (v != null){
            bakover.append(',').append(' ').append(v.verdi);
            v = v.forrige;
        }

        bakover.append(']');
        return  bakover.toString();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {

            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(){
            Node<T> p,q,r;
        if(!fjernOK) throw new IllegalStateException("Ulovlig tilstand!");
        if(endringer != iteratorendringer){
            throw new ConcurrentModificationException("De er like");
        }
        fjernOK = false;
        if(antall ==1){
            hode = hale= null;
        } else if (denne == null){
            hale = hale.forrige;
            hale.neste= null;

        }else if(denne.forrige == hode){
            hode = denne;
            denne.forrige =null;
        }else{
            p= denne.forrige;
            q= p.neste;
            r= p.forrige;
            q.forrige = r;
            r.neste = q;
        }
        antall--;
        endringer++;
        iteratorendringer++;

        }

    }
    //Oppgave 10

    public  static  <T> void sorter (Liste<T> liste, Comparator <? super T> c) {
        for (int i = liste.antall(); i> 0 ; i--){
            Iterator<T> iter = liste.iterator();
            T SortetElement = iter.next();
            int SortetElementIndeks=0;
            for (int j=1; j < i ; j++){
                T temp = iter.next();
                if(c.compare(temp,SortetElement)< 0 ) {
                    SortetElement = temp;
                    SortetElementIndeks = j;
                }
            }
            liste.leggInn(liste.fjern(SortetElementIndeks));
        }
    }
    // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }}
