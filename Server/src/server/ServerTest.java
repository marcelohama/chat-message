package server;

import javax.swing.JFrame;

public class ServerTest
{
   public static void main( String args[] )
   {
      Server application = new Server(); // cria o servidor
      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      application.waitForPackets(); // executa o aplicativo servidor
   } // fim de main
} // fim da classe ServerTest
