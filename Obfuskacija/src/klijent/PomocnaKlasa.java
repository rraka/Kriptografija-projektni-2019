/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klijent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;

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

    public static KeyPair procitajKljuceve(String putanjaDoKeyStora) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(putanjaDoKeyStora));
            KeyPair kljucevi = (KeyPair) ois.readObject();
            ois.close();
            return kljucevi;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
