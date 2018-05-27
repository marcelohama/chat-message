package server;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Server extends JFrame  {
    
    JLabel titulo = new JLabel("     CHAT SERVER");
    // exibe os pacotes recebidos
    private JTextArea output;
    private JScrollPane jScrollPane_IL;
    // socket para conectar ao cliente
    private DatagramSocket socket;
    // lista de endereços
    private List<Integer> porta = new ArrayList<Integer>();
    private List<InetAddress> ip = new ArrayList<InetAddress>();

    // configura o DatagramSocket e a GUI
    public Server() {
        super( "Server" );
        
        output = new JTextArea(); // cria displayArea
        output.setPreferredSize(new java.awt.Dimension(361, 206));
        output.setFont(new java.awt.Font("Tahoma",0,12));
        output.setForeground(new java.awt.Color(0,0,160));
        output.setColumns(20);
        output.setRows(5);
        output.setEditable(false);
        
        titulo.setVisible(true);
        titulo.setSize(200,30);
        add( titulo, BorderLayout.NORTH );

        this.setLayout(null);
        // configura o tamanho da janela
        setSize( 400, 300 );
        // mostra a janela
        setVisible( true );
        
        jScrollPane_IL = new JScrollPane(output);
        add(jScrollPane_IL);
        jScrollPane_IL.setBounds(14, 35, 364, 224);

        try {
            socket = new DatagramSocket( 5000 );
        }
        catch ( SocketException socketException ) {
            socketException.printStackTrace();
            System.exit( 1 );
        }
    }

    public void waitForPackets() {
        while ( true ) {
            // recebe o pacote, exibe o conteudo, retorna uma copia ao cliente
            try {
                // configura o pacote
                byte data[] = new byte[ 100 ];
                DatagramPacket receivePacket = new DatagramPacket( data, data.length );
                // espera receber algum pacote de cliente
                socket.receive( receivePacket );
                // exibe informacoes a partir do pacote recebido de clientes
                //displayMessage(new String(receivePacket.getData(), 0, receivePacket.getLength()) +"\n" );
                // registra o endereço de quem enviou
                boolean flag = false;
                for(int i=0; i<porta.size(); i++) {
                    if(porta.get(i) == receivePacket.getPort()) {
                        flag = true;
                    }
                    if(ip.get(i) == receivePacket.getAddress()) {
                        flag = true;
                    }
                }
                if(flag == false) {
                    porta.add(receivePacket.getPort());
                    ip.add(receivePacket.getAddress());
                    // imprime os ips conectados
                    output.setText("IP's conectados:\n");
                    for(int j=0; j<ip.size(); j++)
                        displayMessage(ip.get(j).toString()+"\n");
                    
                }
                // ecoa na caixa de dos clientes
                sendPacketToClients( receivePacket );
            }
            catch ( IOException ioException ) {
                displayMessage( ioException.toString() + "\n" );
                ioException.printStackTrace();
            }
        }
    }

    // ecoa o pacote para o cliente
    private void sendPacketToClients( DatagramPacket receivePacket ) throws IOException {
        // envia a todos os contatos
        for(int i=0; i<porta.size(); i++) {
            // cria o pacote a enviar
            DatagramPacket sendPacket = new DatagramPacket(
                receivePacket.getData(), receivePacket.getLength(),
                ip.get(i), porta.get(i)
            );
            // envia o pacote aos clientes
            socket.send( sendPacket );
        }
    }

    // manipula a displayArea na thread de despacho de eventos
    private void displayMessage( final String messageToDisplay ) {
        SwingUtilities.invokeLater(
            new Runnable() {
                // atualiza a displayArea
                public void run() {
                    // exibe a mensagem
                    output.append( messageToDisplay );
                }
            }
        );
    }
    
}
