package client;

import javax.swing.JFrame;

public class ClientTest1 {
    public static void main( String args[] ) {
        Client application = new Client();
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.waitForPackets();
    }
}
