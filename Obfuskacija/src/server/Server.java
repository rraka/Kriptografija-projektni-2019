package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author raka
 */
public class Server {
    
    private static final int PORT = 9000;
    private static int brojac = 1;
    
    
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Server pokrenut...!");
        while(true){
            Socket soket = ss.accept();
            new ServerThread(soket, brojac);
            brojac++;
        }
    }
    
}
