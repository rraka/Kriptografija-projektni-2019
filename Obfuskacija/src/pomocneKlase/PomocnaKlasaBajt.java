/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomocneKlase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.crypto.SecretKey;
import poruka.Poruka;

/**
 *
 * @author Raka
 */
public class PomocnaKlasaBajt{
    
    public static byte[] pretvoriPorukuUBajte(Poruka poruka) throws Exception {
         try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            if (poruka.getDodatak() instanceof SecretKey) {
                SecretKey sesijskiKljuc = (SecretKey) poruka.getDodatak();
                poruka.setDodatak(null);
                oos.writeObject(poruka);
                oos.writeObject(sesijskiKljuc);
            } else {
                oos.writeObject(poruka);
            }
            return bos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    

    public static Poruka pretvoriBajtUPoruku(byte[] bajtovi) throws Exception {
        try {
            ByteArrayInputStream bo = new ByteArrayInputStream(bajtovi);
            ObjectInputStream oo = new ObjectInputStream(bo);
            Object p = oo.readObject();
            try {
                Object s = oo.readObject();
                SecretKey ss = (SecretKey) s;
                Poruka pp = (Poruka) p;
                pp.setDodatak(ss);
                return pp;
            } catch (Exception ex) {
             //   ex.printStackTrace();
            }
            return (Poruka) p;
        } catch (Exception ex) {
         //   ex.printStackTrace();
        }
        return null;
    }
}
