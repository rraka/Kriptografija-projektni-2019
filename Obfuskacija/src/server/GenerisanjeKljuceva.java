/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import static pomocneKlase.PomocnaKlasaKriptografija.procitajKljuceve;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 *
 * @author Raka
 */
public class GenerisanjeKljuceva {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        // snimiKljuceve(generisiParKljuceva());
        char[] lozinka = new char[]{'l', 'o', 'z', 'i', 'n', 'k', 'a'};
       // System.out.println(hashLozinke(lozinka));
       // System.out.println("PROVJERA LOZINKE: " + pomocneKlase.PomocnaKlasaKriptografija.ispravnaLozinka(lozinka, "PVzaJ2F4Xa5YgwKg4oQqv8PZKF569ireKLLnaR/mdWU=", "PlZIxygn9IaPU8yuGy4Mt4R9xU8="));
        
//            KeyPair parKljuceva = procitajKljuceve("./kljucevi/");
//            System.out.println("Javni : " + parKljuceva.getPublic().toString());
//            System.out.println("privatni : " + parKljuceva.getPrivate().toString());
    }

    public static KeyPair generisiParKljuceva() {
        KeyPairGenerator generatorParaKljuceva;
        KeyPair parKljuceva = null;
        try {
            generatorParaKljuceva = KeyPairGenerator.getInstance("RSA");
            generatorParaKljuceva.initialize(8192);
            parKljuceva = generatorParaKljuceva.genKeyPair();
            snimiKljuceve(parKljuceva);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return parKljuceva;
    }

    public static void snimiKljuceve(KeyPair keyPair) throws IOException {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Store Public Key.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(new File("./kljucevi/caPublic.pem"));
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();

        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());
        fos = new FileOutputStream("./kljucevi/caPrivate.pem");
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
    }

    //kreiranje salta i hesirane lozinke
    public static String hashLozinke(char[] lozinka) throws NoSuchAlgorithmException {

        //Security.addProvider(new BouncyCastleProvider());
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);

        Base64.Encoder base64 = Base64.getEncoder();
        System.out.println("Salt: " + base64.encodeToString(salt));

        MessageDigest md = MessageDigest.getInstance("SHA256");
        md.update(salt);

        byte[] hash = md.digest(String.valueOf(lozinka).getBytes());
        String izlazHash = base64.encodeToString(hash);
        provjeraLozinke(lozinka, base64.encodeToString(salt));
        return izlazHash;

    }

    public static boolean provjeraLozinke(char[] lozinka,  String salt) throws NoSuchAlgorithmException {

        boolean provjera = false;

        //System.out.println("Primljeni salt: " + salt);
        Base64.Encoder encode = Base64.getEncoder();
        Base64.Decoder decode = Base64.getDecoder();
        byte[] saltIzStringa = decode.decode(salt);
       // System.out.println("Dekodovan salt iz byte[]: " + encode.encodeToString(saltIzStringa));

        return true;
    }
}
