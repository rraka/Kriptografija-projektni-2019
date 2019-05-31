/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import poruka.Poruka;

/**
 *
 * @author Raka
 */
class ServerThread extends Thread {

    private Socket soket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private HashMap<String, Integer> prebrojeniLeci;
    private int brojac;

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
            while (true) {

                Poruka poruka = (Poruka) ois.readObject();
                if (poruka.getPoruka().equals("prijava")) {

                    File putanjaDoKorisnika = new File(".\\korisnici\\korisnici.txt");

                    /*primiFajl(new File(putanjaZaZipFajl.getPath() + "\\leci.zip"));

                    File putanjaDoUnZipFajla = new File(".\\src\\ServerArhiver\\fajlovi\\unZipovani\\" + brojac);
                    if (!putanjaDoUnZipFajla.exists()) {
                        putanjaDoUnZipFajla.mkdir();
                    }
                    unZip(new File(putanjaZaZipFajl.getPath() + "\\leci.zip"), putanjaDoUnZipFajla);
                    razvrstajLetke(putanjaDoUnZipFajla);
                    prebrojeniLeci = prebrojLetke();
                    // oos.writeObject(new Poruka("razvrstani leci", razvrstaniLeci));
                    oos.writeObject(new Poruka("broj letaka", prebrojeniLeci));
                }*/
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
