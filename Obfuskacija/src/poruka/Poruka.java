/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poruka;

import java.io.Serializable;
import javax.crypto.SecretKey;
import pomocneKlase.Potpis;

/**
 *
 * @author Raka
 */
public class Poruka implements Serializable{
    
    public enum IdPoruke {
        SESIJSKI_KLJUC,
        PRIJAVA,
        PRIJAVA_USPJESNA,
        PRIJAVA_NEUSPJESNA,
        VERIFIKACIJA_POTPISA_USPJESNA,
        VERIFIKACIJA_POTPISA_NEUSPJESNA,
        SPISAK_DATOTEKA,
        SLANJE_FAJLA,
        ZAVRSENO_SLANJE_FAJLA,
        PREUZIMANJE_FAJLA,
        ZAVRSENO_PREUZIMANJE_FAJLA,
    }
    
    private IdPoruke idPoruke = null;
    private String korisnickoIme = null;
    private char[] lozinka = null;
    private Potpis potpis = null;
    private Object dodatak = null;

    public Poruka(){
    }
    
    public Poruka(IdPoruke idPoruke){
        this.idPoruke = idPoruke;
    }

    public Poruka(IdPoruke idPoruke, String korisnickoIme, char[] lozinka) {
        this.idPoruke = idPoruke;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }
    
    public Poruka(IdPoruke idPoruke, Object dodatak, Potpis potpis) {
        this.idPoruke = idPoruke;
        this.dodatak = dodatak;
        this.potpis =  potpis;
    }
    
    
    public Poruka(IdPoruke idPoruke, Object dodatak) {
        this.idPoruke = idPoruke;
        this.dodatak = dodatak;
    }

    public IdPoruke getIdPoruke() {
        return idPoruke;
    }

    public void setIdPoruke(IdPoruke idPoruke){
        this.idPoruke = idPoruke;
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

    public Potpis getPotpis() {
        return potpis;
    }

    public void setPotpis(Potpis potpis) {
        this.potpis = potpis;
    }
    
    
    
    @Override
    public String toString(){
        return idPoruke + " / " + dodatak;
    }
    
    
    
}

