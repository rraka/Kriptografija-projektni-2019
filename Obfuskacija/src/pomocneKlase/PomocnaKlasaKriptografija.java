/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomocneKlase;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import poruka.Poruka;
import server.Server;
import pomocneKlase.Potpis;
        
        /**
 *
 * @author Raka
 */
public class PomocnaKlasaKriptografija {

    private static String simetricniAlgoritam = null;

    /*
    public static KeyPair procitajKljuceve(String putanjaDoKljuceva) throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {

        Security.addProvider(new BouncyCastleProvider());

        // String privatniKljuc = new String(bajtoviKljuca, "UTF-8");
        //  privatniKljuc = privatniKljuc.replaceAll("(-+BEGIN RSA PRIVATE KEY-+\\r?\\n|-+END RSA PRIVATE KEY-+\\r?\\n?)", "");
        //  BASE64Decoder decoder = new BASE64Decoder();
        //  bajtoviKljuca = decoder.decodeBuffer(privatniKljuc);
        //privatni kljuc iz DER formata
        System.out.println("PUTANJA DO KLJUCA" + putanjaDoKljuceva + "Private.pem");
        byte[] bajtoviKljuca = Files.readAllBytes(Paths.get(putanjaDoKljuceva + "Private.pem"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bajtoviKljuca);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privatniKljuc = kf.generatePrivate(keySpec);
        /*
        byte[] bajtoviKljuca1 = Files.readAllBytes(Paths.get("./kljucevi/markoPublic.der"));
        X509EncodedKeySpec keySpec1 = new X509EncodedKeySpec(bajtoviKljuca1);
        KeyFactory kf1 = KeyFactory.getInstance("RSA");
        PublicKey pub = kf1.generatePublic(keySpec1);
         // 

        //eksportovanje javnog kljuca iz privatnog
        RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) privatniKljuc;
        RSAPublicKeySpec publicKey = new RSAPublicKeySpec(privateKey.getModulus(), privateKey.getPublicExponent());
        PublicKey javniKljuc = kf.generatePublic(publicKey);

        //kodovano u Base64 zbog ispisa
        byte[] privatniBajt = privateKey.getEncoded();
        byte[] javniBajt = javniKljuc.getEncoded();
        String privatniString = Base64.getEncoder().encodeToString(privatniBajt);
        String javniString = Base64.getEncoder().encodeToString(javniBajt);

        System.out.println("javni NAJNOVIJE   K L J U C:::::: " + javniKljuc);
        System.out.println("P R I V A T N I    K L J U C::::: " + privatniKljuc);
        System.out.println("J A V N I          K L J U C::::: " + javniKljuc);
        KeyPair parKljuceva = new KeyPair(javniKljuc, privatniKljuc);

        return parKljuceva;
    }
     */

    public static byte[] potpisivanjePoruke(Poruka poruka, PrivateKey kljuc) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, Exception {

        String hesAlgoritmi[] = {"SHA1withRSA", "SHA256withRSA"};
        Random rand = new Random();
        int slucajanIzbor = rand.nextInt(2);

        byte[] bajtoviPoruke = PomocnaKlasaBajt.pretvoriPorukuUBajte(poruka);
        System.out.println("PROVJERA OBJEKTA U POTPISIVANJE PORUKE - porukaUbajtove: " + bajtoviPoruke);
        Signature potpisivanje = Signature.getInstance(hesAlgoritmi[slucajanIzbor]);
        potpisivanje.initSign(kljuc);
        potpisivanje.update(bajtoviPoruke);
        byte[] potpisanaPoruka = potpisivanje.sign();
        System.out.println("PROVJERA OBJEKTA U POTPISIVANJE PORUKE - potpisana poruka bajtovi: " + potpisanaPoruka);
        System.out.println("====Potpisana poruka: " + potpisanaPoruka.toString());
        poruka.setPotpis(new Potpis(potpisanaPoruka, hesAlgoritmi[slucajanIzbor]));
        return potpisanaPoruka;
    }

    public static boolean verifikacijaPotpisaPoruke(Poruka poruka, PublicKey kljuc) {
        try {
            Potpis potpis =  (Potpis) poruka.getPotpis();
            poruka.setPotpis(null);

            byte[] bajtoviPoruke = PomocnaKlasaBajt.pretvoriPorukuUBajte(poruka);
            Signature potpisivanje = Signature.getInstance(potpis.getAlgoritam());
            potpisivanje.initVerify(kljuc);
            potpisivanje.update(bajtoviPoruke);
            boolean verifikovano = potpisivanje.verify(potpis.getPotpis());
            System.out.println("VERIFIKOVAN POTPIS: " + verifikovano);
            poruka.setPotpis(potpis);
            return verifikovano;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static SecretKey generisiSesijskiKljuc() {
        try {
            String[] simetricniAlgoritmi = {"AES", "DES"};
            Random rand = new Random();
            int slucajanIzbor = rand.nextInt(2);

            KeyGenerator generator = KeyGenerator.getInstance(simetricniAlgoritmi[slucajanIzbor]);
            SecretKey kljuc = generator.generateKey();
            return kljuc;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] enkriptujJavnimKljucem(byte[] poruka, PublicKey javni) { //sa asimetricnim kriptujem simetricni kljuc
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, javni);
            byte[] sifrovano = cipher.doFinal(poruka);
            return sifrovano;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] dekripcijaPrivatnimKljucem(byte[] poruka, PrivateKey kljuc) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            System.out.println("DEKRIPCIJA PRIVATNIM SERVERA, privatniServera iz argumenta: " + kljuc);
            cipher.init(Cipher.DECRYPT_MODE, kljuc);
            byte[] original = cipher.doFinal(poruka);
            return original;
        } catch (Exception ex) {
           // ex.printStackTrace();
        }
        return null;
    }

    public static byte[] simetricnaEnkripcija(byte[] porukaUBajtovima, SecretKey sesijskiKljuc) {
        try {
            Cipher cipher = Cipher.getInstance(sesijskiKljuc.getAlgorithm() + "/ECB/PKCS5Padding", "BC");
            System.out.println("AlGORITAM SESIJSKOG KLJUCA: " + sesijskiKljuc.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, sesijskiKljuc);
            byte[] sifrovano = cipher.doFinal(porukaUBajtovima);
            return sifrovano;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] simetricnaDekripcija(byte[] ulaz, SecretKey sesijskiKljuc) {
        try {
            Cipher cipher = Cipher.getInstance(sesijskiKljuc.getAlgorithm() + "/ECB/PKCS5Padding", "BC");
            System.out.println("AlGORITAM SESIJSKOG KLJUCA ENKRIPCIJA: " + sesijskiKljuc.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, sesijskiKljuc);
            byte izlaz[] = cipher.doFinal(ulaz);
            return izlaz;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean provjeraLozinke(char[] lozinka, String hash, String salt) throws NoSuchAlgorithmException{
        
        Base64.Decoder decoder = Base64.getDecoder();
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bajtoviLozinke = String.valueOf(lozinka).getBytes();
        byte[] bajtoviSalta = decoder.decode(salt);
        
        MessageDigest md = MessageDigest.getInstance("SHA256");
        md.update(bajtoviSalta);
        byte[] bajtoviHash = md.digest(bajtoviLozinke);
        String izracunatiHash = encoder.encodeToString(bajtoviHash);
        
        if(izracunatiHash.equals(hash)){
            return true;
        }
        else return false;
        
    }

    public static KeyPair procitajKljuceve(String putanjaDoKljuceva) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Read Public Key.
        File filePublicKey = new File(putanjaDoKljuceva + "Public.pem");
        FileInputStream fis = new FileInputStream(putanjaDoKljuceva + "Public.pem");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(putanjaDoKljuceva + "Private.pem");
        fis = new FileInputStream(putanjaDoKljuceva + "Private.pem");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

}
