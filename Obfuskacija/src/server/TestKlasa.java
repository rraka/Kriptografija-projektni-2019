/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import pomocneKlase.PomocnaKlasaKriptografija;

/**
 *
 * @author Raka
 */
public class TestKlasa {

    public static void main(String[] args) throws Exception {
        KeyPair parKljucevaServera = PomocnaKlasaKriptografija.procitajKljuceve("./kljucevi/server");
        PublicKey javniKljucServera = parKljucevaServera.getPublic();
        PrivateKey privatniKljucServera = parKljucevaServera.getPrivate();
        // javniKljucServera = sertifikat.getPublicKey();
        System.out.println("JAVNI KLJUC SERVERA: " + javniKljucServera.toString() + "\n\n privatni kljuc: " + privatniKljucServera + "\n\n");

        FileInputStream fis = new FileInputStream(new File(".\\sertifikati\\server.crt"));
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate sertifikat = (X509Certificate) cf.generateCertificate(fis);
        javniKljucServera = sertifikat.getPublicKey();
        System.out.println("JAVNI KLJUC klijenta iz SERTIFIKATA u serverTHREAD: " + javniKljucServera);
    }

}
