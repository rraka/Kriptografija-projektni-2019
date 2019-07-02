package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import pomocneKlase.PomocnaKlasaKriptografija;

/**
 *
 * @author raka
 */
public class Server {
    
    private static final int PORT = 9000;
    private static int brojac = 1;
    public static PrivateKey privatniKljucServera;
    public static PublicKey javniKljucServera;
    
    
    public static void main(String[] args) throws Exception{
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Server pokrenut...!");
        KeyPair parKljuceva = PomocnaKlasaKriptografija.procitajKljuceve("./kljucevi/server");
        privatniKljucServera = parKljuceva.getPrivate();
        javniKljucServera = parKljuceva.getPublic();
        System.out.println("Serverskki kljucevi: javni: " + javniKljucServera + "\nprivatni: " + privatniKljucServera);
        while(true){
            Socket soket = ss.accept();
            new ServerThread(soket, brojac);
            brojac++;
        }
    }
    
}
