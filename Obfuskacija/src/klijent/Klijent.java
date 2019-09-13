/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klijent;

/**
 *
 * @author Raka
 */
public class Klijent {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                new LoginGUI().setVisible(true);
                System.out.println("Poslije pokretanja ED u KLijent klasi");
            }
        });
    }
}
