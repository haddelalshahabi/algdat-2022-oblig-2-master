package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;


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
            throw new UnsupportedOperationException("Tabellen er en null!!");
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

    public Liste<T> subliste(int fra, int til) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int antall() {
        //retunerer antall verdier
        return antall;
    }

    @Override
    public boolean tom() {
        //returnere true/false avhengig av om listen er tom eller ikke
        if (antall() == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean leggInn(T verdi) {
        //oppgave 2 b)
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
        //oppgave 2 b)
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T hent(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        throw new UnsupportedOperationException();
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
            Node<T> slett = prøvNode(index);

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


        //  throw new UnsupportedOperationException();


    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
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
        public void remove() {
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

} // class DobbeltLenketListe


