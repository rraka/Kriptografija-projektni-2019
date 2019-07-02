/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomocneKlase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import poruka.Poruka;
import pomocneKlase.Potpis;

/**
 *
 * @author Raka
 */
public class PomocnaKlasaSlanjePrimanje {

    public static void posaljiPorukuKriptovanuJavnim(ObjectOutputStream out, Poruka poruka, PublicKey javniKljucServera, PrivateKey privatniKljucKlijenta) {
        try {
            byte[] bajtoviPoruke = PomocnaKlasaBajt.pretvoriPorukuUBajte(poruka);
           // byte[] potpisPoruke = PomocnaKlasaKriptografija.potpisivanjePoruke(poruka, privatniKljucKlijenta);
           // System.out.println("PROVJERA OBJEKTA U POSALJI KRIPO PORUKU  - vracena potpisana poruka: " + potpisPoruke.toString());
            byte[] kriptovanaPoruka = PomocnaKlasaKriptografija.enkriptujJavnimKljucem(bajtoviPoruke, javniKljucServera);
            System.out.println("J A V N I K LJ U C S E R V E R A : " + javniKljucServera);
            System.out.println("PRIJE SLANJA SERVERU SESIJSKOG KLJUCA: " + kriptovanaPoruka.toString());
            out.writeObject(kriptovanaPoruka);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
    }

    public static void posaljiPotpisanuPorukuKriptovanuSesijskim(ObjectOutputStream out, Poruka poruka, PrivateKey privatniKljuc, SecretKey sesijskiKljuc) throws Exception {
        
            PomocnaKlasaKriptografija.potpisivanjePoruke(poruka, privatniKljuc);
            byte bajtoviPoruke[] = PomocnaKlasaBajt.pretvoriPorukuUBajte(poruka);
            byte kriptovaniBajtovi[] = PomocnaKlasaKriptografija.simetricnaEnkripcija(bajtoviPoruke, sesijskiKljuc);
            out.writeObject(kriptovaniBajtovi);
        
    }

    public static Poruka primiPotpisanuPoruku(ObjectInputStream in, PublicKey javniKljuc, SecretKey sesijskiKljuc) throws Exception {
        try {
            byte kriptovanaPorukaUBajtovima[] = (byte[]) in.readObject();
            byte dekriptovaniBajti[] = PomocnaKlasaKriptografija.simetricnaDekripcija(kriptovanaPorukaUBajtovima, sesijskiKljuc);
            Poruka poruka = (Poruka) PomocnaKlasaBajt.pretvoriBajtUPoruku(dekriptovaniBajti);
            poruka.getPotpis().setVerifikovan(PomocnaKlasaKriptografija.verifikacijaPotpisaPoruke(poruka, javniKljuc));
             return poruka;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

//    public static void posaljiFajl(ObjectOutputStream out, String putanja, PrivateKey privatniKljuc, SecretKey sesijskiKljuc) {
//        try {
//            byte[] bafer = new byte[1 * 1024 * 1024];
//            File fajlZaSlanje = new File(putanja);
//            InputStream fajl = new FileInputStream(fajlZaSlanje);
//            Poruka poruka = null;
//            int duzinaBafera = 0;
//            while ((duzinaBafera = fajl.read(bafer)) > 0) {
//                poruka = new Poruka(IdPoruke.SLANJE_FAJLA, bafer);
//                poruka.setSadrzaj("" + duzinaBafera);
//                posaljiPotpisanuPorukuKriptovanuSesijskim(out, poruka, privatniKljuc, sesijskiKljuc);
//            }
//            fajl.close();
//
//            poruka = new Poruka(IdPoruke.ZAVRSENO_SLANJE_FAJLA, "Kraj slanja");
//            posaljiPotpisanuPorukuKriptovanuSesijskim(out, poruka, privatniKljuc, sesijskiKljuc);
//        } catch (Exception ex) {
//            Logger.getLogger(PomocnaKlasaSlanje.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

//    public static void preuzmiFajl(ObjectInputStream in, String nazivFajla, PublicKey javniKljuc, SecretKey sesijskiKljuc) {
//        try {
//            File noviFajl = new File("c:\\tmp\\" + nazivFajla);
//            OutputStream fajl = new FileOutputStream(noviFajl);
//            int kontrolnaDuzina = 0, flag = 0;
//            byte[] bafer = new byte[1 * 1024 * 1024];
//            while (true) {
//                Poruka odgovorServera = primiPotpisanuPoruku(in, javniKljuc, sesijskiKljuc);
//                if (odgovorServera == null || !odgovorServera.getPotpis().isVerifikovan() || odgovorServera.getIdPoruke() != IdPoruke.PREUZIMANJE_FAJLA) {
//                    break;
//                }
//                kontrolnaDuzina = Integer.parseInt(odgovorServera.getSadrzaj());
//                bafer = (byte[]) odgovorServera.getDodatak();
//                fajl.write(bafer, 0, kontrolnaDuzina);
//            }
//            fajl.close();
//        } catch (Exception ex) {
//            Logger.getLogger(PomocnaKlasaSlanjePrimanje.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}

    

