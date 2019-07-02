/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomocneKlase;

import java.io.Serializable;

/**
 *
 * @author LJUBO
 */
public class Potpis implements Serializable {

    private byte[] potpis;
    private String hesAlgoritam;
    private boolean verifikovan;
  //  private string probaPulla;

    public Potpis(byte[] potpis, String hesAlgoritam) {
        this.potpis = potpis;
        this.hesAlgoritam = hesAlgoritam;
    }

    public byte[] getPotpis() {
        return potpis;
    }

    public void setPotpis(byte[] potpis) {
        this.potpis = potpis;
    }

    public String getAlgoritam() {
        return hesAlgoritam;
    }

    public void setAlgoritam(String hesAlgoritam) {
        this.hesAlgoritam = hesAlgoritam;
    }

    public boolean isVerifikovan() {
        return verifikovan;
    }

    public void setVerifikovan(boolean verifikovan) {
        this.verifikovan = verifikovan;
    }
    
}
