/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import javax.crypto.SecretKey;
import pomocneKlase.PomocnaKlasaBajt;
import pomocneKlase.PomocnaKlasaKriptografija;
import pomocneKlase.PomocnaKlasaSlanjePrimanje;
import poruka.Poruka;

/**
 *
 * @author Raka
 */
class ServerThread extends Thread {

    private Socket soket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private SecretKey sesijskiKljuc;
    private PublicKey javniKljucKlijenta;
    private int brojac;
    private String korisnickoIme;

    public ServerThread(Socket soket, int brojac) {
        try {
            this.soket = soket;
            this.brojac = brojac;
            this.ois = new ObjectInputStream(soket.getInputStream());
            this.oos = new ObjectOutputStream(soket.getOutputStream());
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Poruka poruka = null;
            byte[] dekriptovaniBajtovi = null;
            while (true) {
                byte[] bajtovi = (byte[]) ois.readObject();
                poruka = PomocnaKlasaBajt.pretvoriBajtUPoruku(bajtovi);
                if (poruka == null) {
                    System.out.println("Privatni kljuc servera: " + Server.privatniKljucServera);
                    dekriptovaniBajtovi = PomocnaKlasaKriptografija.dekripcijaPrivatnimKljucem(bajtovi, Server.privatniKljucServera);
                    poruka = PomocnaKlasaBajt.pretvoriBajtUPoruku(dekriptovaniBajtovi);
                }
                if (poruka == null) {
                    dekriptovaniBajtovi = PomocnaKlasaKriptografija.simetricnaDekripcija(bajtovi, sesijskiKljuc);//pokusaj dekr sa sesijskim
                    poruka = PomocnaKlasaBajt.pretvoriBajtUPoruku(dekriptovaniBajtovi);
                    System.out.println("Iz DEKRIPCIJA SIMETRICNIM KLJUCEM");
                    System.out.println("Iz DEKRIPCIJA SIMETRICNIM KLJUCEM" + poruka.getIdPoruke());
                }

                if (poruka == null) {
                    continue;
                }
////                if (poruka == null) {
////                    dekriptovaniBajtovi = dekripcijaJednimOdSesijskih(bajtovi);
////                    poruka = PomocnaKlasaBajtObjekat.pretvoriBajtUPoruku(dekriptovaniBajtovi);
////                }
//                if (poruka == null) {
//                     System.out.println("PORUKA == NULL CONTINUE " );
//                    continue;
//                }
                if (poruka.getIdPoruke() == Poruka.IdPoruke.PRIJAVA) {
                    korisnickoIme = poruka.getKorisnickoIme();
                    FileInputStream fis = new FileInputStream(new File("./sertifikati/" + korisnickoIme + ".crt"));
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    X509Certificate sertifikat = (X509Certificate) cf.generateCertificate(fis);
                    javniKljucKlijenta = sertifikat.getPublicKey();
                    System.out.println("JAVNI KLJUC KLIJENTA: \n" + javniKljucKlijenta);
                    boolean verifikacijaPotpisa = PomocnaKlasaKriptografija.verifikacijaPotpisaPoruke(poruka, javniKljucKlijenta);
                    if (verifikacijaPotpisa == true) {
                        boolean ispravnaLozinka = false;
                        String procitanoKorisnickoIme = null;
                        BufferedReader citaj = new BufferedReader(new FileReader(new File("./korisnici/korisnici.txt")));
                        String procitanaLinija;
                        while ((procitanaLinija = citaj.readLine()) != null) {

                            String[] vrijednosti = procitanaLinija.split(",");
                            if (poruka.getKorisnickoIme().equals(vrijednosti[0])) {         //provjera korisnickog imena
                                if (PomocnaKlasaKriptografija.provjeraLozinke(poruka.getLozinka(), vrijednosti[1], vrijednosti[2]) == true) {
                                    ispravnaLozinka = true;
                                    procitanoKorisnickoIme = vrijednosti[0];
                                } else {
                                    ispravnaLozinka = false;
                                }
                            }
                        }
                        if (ispravnaLozinka == true && poruka.getKorisnickoIme().equals(procitanoKorisnickoIme)) {
                            System.out.println("ispravnaLOZINKA: " + ispravnaLozinka);
                            System.out.println("korisnickoIME: " + procitanoKorisnickoIme);

                            System.out.println("JAVNI KLJUC klijenta iz SERTIFIKATA u serverTHREAD: " + javniKljucKlijenta);
                            System.out.println("USPJESNO LOGOVANJE!!");
                            PomocnaKlasaSlanjePrimanje.posaljiPotpisanuPorukuKriptovanuSesijskim(oos, new Poruka(Poruka.IdPoruke.PRIJAVA_USPJESNA), Server.privatniKljucServera, sesijskiKljuc);
                        } else {
                            PomocnaKlasaSlanjePrimanje.posaljiPotpisanuPorukuKriptovanuSesijskim(oos, new Poruka(Poruka.IdPoruke.PRIJAVA_NEUSPJESNA), Server.privatniKljucServera, sesijskiKljuc);
                            System.out.println("NEUSPJESNO LOGOVANJE!!");
                        }
                    } else {
                        System.out.println("NEUSPJESNA VERIFIKACIJA POTPISA!!");
                        PomocnaKlasaSlanjePrimanje.posaljiPotpisanuPorukuKriptovanuSesijskim(oos, new Poruka(Poruka.IdPoruke.VERIFIKACIJA_POTPISA_NEUSPJESNA), Server.privatniKljucServera, sesijskiKljuc);
                    }

                } else if (poruka.getIdPoruke() == Poruka.IdPoruke.SESIJSKI_KLJUC) {
                    sesijskiKljuc = (SecretKey) poruka.getDodatak();
                    System.out.println("Sesijski kljuc: " + sesijskiKljuc.toString());
//                    byte[] bajtoviPotpisa = (byte[]) ois.readObject();
//                    byte[] dekriptovaniBajtoviPotpisa = PomocnaKlasaKriptografija.simetricnaDekripcija(bajtoviPotpisa, sesijskiKljuc);
//                    Poruka porukaPotpisa = PomocnaKlasaBajt.pretvoriBajtUPoruku(dekriptovaniBajtoviPotpisa);
//                    boolean provjera = PomocnaKlasaKriptografija.verifikacijaPotpisaPoruke(porukaPotpisa, javniKljucKlijenta); //voditi racuna da nije null?!
//                    System.out.println(provjera);
//                    if (!provjera) {
//                        sesijskiKljuc = null;
//                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void primiFajl(File putanjaFajla) {
        try {
            System.out.println("Preuzimanje pocinje...");
            long duzinaLong = (long) ois.readObject();
            int duzina = (int) duzinaLong;
            int kontrolnaDuzina = 0, flag = 0;
            byte[] buffer = new byte[2 * 1024 * 1024];
            OutputStream fajl = new FileOutputStream(putanjaFajla);
            while ((kontrolnaDuzina = ois.read(buffer)) > 0) {
                fajl.write(buffer, 0, kontrolnaDuzina);
                flag += kontrolnaDuzina;
                if (duzina <= flag) {
                    break;
                }
            }
            System.out.println("Preuzimanje zavrseno...");
            fajl.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
