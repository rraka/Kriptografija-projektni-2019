/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poruka;

import java.io.Serializable;

/**
 *
 * @author Raka
 */
public class Poruka implements Serializable{
    
    public enum IdPoruke {
        RAZMJENA_SESIJSKOG_KLJUCA,
        PROVJERA_PODATAKA_LOGOVANJA,
        USPJESNA_PRIJAVA,
        NEUSPJESNA_PRIJAVA,
        SPISAK_DATOTEKA,
        SLANJE_FAJLA,
        ZAVRSENO_SLANJE_FAJLA,
        PREUZIMANJE_FAJLA,
        ZAVRSENO_PREUZIMANJE_FAJLA,
    }
    
    private String poruka;
    private String korisnickoIme;
    private char[] lozinka;
    private Object dodatak;

    
    public Poruka(String poruka){
        this.poruka = poruka;
    }

    public Poruka(String poruka, String korisnickoIme, char[] lozinka) {
        this.poruka = poruka;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }
    
    public Poruka(String poruka, Object dodatak) {
        this.poruka = poruka;
        this.dodatak = dodatak;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public Object getDodatak() {
        return dodatak;
    }

    public void setDodatak(Object dodatak) {
        this.dodatak = dodatak;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public char[] getLozinka() {
        return lozinka;
    }

    public void setLozinka(char[] lozinka) {
        this.lozinka = lozinka;
    }
    
    
    
}

