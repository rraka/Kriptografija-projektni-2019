/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 *
 * @author Raka
 */
public class GenerisanjeKljuceva {
    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair parKljuceva = kpg.genKeyPair();
        
       
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./kljucevi/pero.pem"));
            oos.writeObject(parKljuceva);
            oos.close();
           
           
}
}
