package it.uniba.dib.sms222316.Gameplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Property{
        private String nome , tipo , gruppo;
        private int costo , costoQuadro , prezzoVendita;
        private int posizione;
        private String photo;
        private final List<Integer> affitto;
        private int paints;
        private String descrizione;
        private Player Giocatore;

        //Costruttore per l'inizio della partita
        public Property(String nome,  String tipo, String gruppo,  String descrizione , int costo, List<Integer> affitto, int costoQuadro , int prezzoVendita , int posizione, String photo) {
            this.nome = nome;
            this.costo = costo;
            this.affitto = affitto;
            this.tipo = tipo;
            this.gruppo = gruppo;
            this.descrizione = descrizione;
            this.costoQuadro = costoQuadro;
            this.prezzoVendita = prezzoVendita;
            this.posizione = posizione;
            this.paints = 0;
            this.photo = photo;
            this.Giocatore = null;
        }
        public Property() {
            this.affitto = new ArrayList<>();
        }
        @Override
        public Property clone() {

                Property clonedProperty = new Property(this.nome,this.tipo, this.gruppo, this.descrizione, this.costo, this.affitto, this.costoQuadro, this.prezzoVendita, this.posizione, this.photo);
                clonedProperty.photo = this.photo;
                if(this.Giocatore!=null) clonedProperty.Giocatore = this.Giocatore.clone();
                else clonedProperty.Giocatore = null;
            return clonedProperty;
        }


        public String getNome() {
            return nome;
        }

        public int getPosizione() { return posizione; }

        public int getCosto() {
            return costo;
        }

        public int getAffitto(int paint) {
            return affitto.get(paint);
        }

        public String getGruppo() {
            return gruppo;
        }

        public int getCostoQuadro() {
            return costoQuadro;
        }

        public int getCostoVendita() {
            return prezzoVendita;
        }

        public List<Integer> getAffitto() {
            return affitto;
        }

        public String getDescrizione() {
            return descrizione;
        }

        public String getTipo() {
                return tipo;
            }

        public void setPaints(int paints) {
            this.paints = paints;
        }

        public int getPaints() {
            return paints;
        }
        public boolean isAvaible()
        {
            if (this.Giocatore == null)return true;
            else return false;
        }

        public Player getGiocatore() {
        return Giocatore;
    }

        public void setGiocatore(Player giocatore) {
        Giocatore = giocatore;
    }

    public String getphoto() {return photo;
    }

}
