/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klijent;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.security.Security;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import server.Server;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Raka
 */
public class PomocnaKlasa {

    //kreiranje salta i hesirane lozinke
    public static String hashLozinke(char[] lozinka) throws NoSuchAlgorithmException {

        //Security.addProvider(new BouncyCastleProvider());
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);
        Base64.Encoder base64 = Base64.getEncoder();

        System.out.println("Salt prije nextBytes: " + base64.encodeToString(salt));

        MessageDigest md = MessageDigest.getInstance("SHA256");
        md.update(salt);
        byte[] hash = md.digest(String.valueOf(lozinka).getBytes());
        String izlazHash = Base64.getEncoder().encodeToString(hash);
        System.out.println("Pass nakon saltovanja: " + izlazHash);
        return izlazHash;

    }

    public static KeyPair procitajKljuceve(String putanjaDoKljuceva) throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {

        Security.addProvider(new BouncyCastleProvider());

       // String privatniKljuc = new String(bajtoviKljuca, "UTF-8");
      //  privatniKljuc = privatniKljuc.replaceAll("(-+BEGIN RSA PRIVATE KEY-+\\r?\\n|-+END RSA PRIVATE KEY-+\\r?\\n?)", "");
      //  BASE64Decoder decoder = new BASE64Decoder();
      //  bajtoviKljuca = decoder.decodeBuffer(privatniKljuc);
    
        //privatni kljuc iz DER formata
        byte[] bajtoviKljuca = Files.readAllBytes(Paths.get("./kljucevi/markoPrivate.der"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bajtoviKljuca);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privatniKljuc = kf.generatePrivate(keySpec);
        /*
        byte[] bajtoviKljuca1 = Files.readAllBytes(Paths.get("./kljucevi/markoPublic.der"));
        X509EncodedKeySpec keySpec1 = new X509EncodedKeySpec(bajtoviKljuca1);
        KeyFactory kf1 = KeyFactory.getInstance("RSA");
        PublicKey pub = kf1.generatePublic(keySpec1);
        */
        
        //eksportovanje javnog kljuca iz privatnog
        RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) privatniKljuc;
        RSAPublicKeySpec publicKey = new RSAPublicKeySpec(privateKey.getModulus(), privateKey.getPublicExponent());
        PublicKey javniKljuc = kf.generatePublic(publicKey);
        
        
        byte[] privatniBajt = privateKey.getEncoded();
        byte[] javniBajt = javniKljuc.getEncoded();
        String  privatniString = Base64.getEncoder().encodeToString(privatniBajt);
        String javniString = Base64.getEncoder().encodeToString(javniBajt);
        
        System.out.println("javni NAJNOVIJE   K L J U C:::::::::::::::::::::::: " + privatniString);
        System.out.println("P R I V A T N I    K L J U C::::: " + privatniString);
        System.out.println("J A V N I          K L J U C::::: " + javniString);
        KeyPair parKljuceva = new KeyPair(javniKljuc, privatniKljuc);

        return parKljuceva;
    }
}
