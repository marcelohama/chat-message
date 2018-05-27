package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame  {
    
    // para inserir mensagens
    private JTextField input;
    // para exibir mensagens
    private JTextArea output;
    // socket para conectar ao servidor
    private DatagramSocket socket;
    private String ip = "";
    
    // configura o DatagramSocket e a GUI
    public Client() {
        super( "Client" );
        input = new JTextField();
        input.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent event ) {
                    // cria e envia o pacote
                    try {
                        // obtem a mensagem no campo de texto
                        String message = ip+": "+event.getActionCommand();
                        // converte em bytes
                        byte data[] = message.getBytes();
                        // cria sendPacket para o endereço especificado
                        DatagramPacket sendPacket = new DatagramPacket( data, data.length, InetAddress.getLocalHost(), 5000);  
                        // envia o pacote
                        socket.send( sendPacket );
                        output.setCaretPosition(output.getText().length());
                        input.setText("");
                    }
                    catch ( IOException ioException ) {
                        displayMessage( ioException.toString() + "\n" );
                        ioException.printStackTrace();
                    }
                }
            }
        );

        add( input, BorderLayout.SOUTH );
        output = new JTextArea();
        output.setFont(new java.awt.Font("Tahoma",1,11));
        output.setForeground(new java.awt.Color(128,0,0));
        output.setColumns(20);
        output.setRows(5);
        output.setEditable(false);
        add( new JScrollPane( output  ), BorderLayout.CENTER );

        // configura o tamanho da janela
        setSize( 400, 300 );
        // mostra a janela
        setVisible( true );

        // cria DatagramSocket para envio e recebimento de pacotes
        try {
            socket = new DatagramSocket();
            
            try {
                // obtem a mensagem no campo de texto
                String message = " ";
                // converte em bytes
                byte data[] = message.getBytes();
                // cria sendPacket para o endereço especificado
                DatagramPacket sendPacket = new DatagramPacket( data, data.length, InetAddress.getLocalHost(), 5000);
                
                // converte em bytes
                message = ""+sendPacket.getAddress()+" entrou no chat";
                ip = sendPacket.getAddress()+"";
                data = message.getBytes();
                sendPacket.setData(data);
                        
                // envia o pacote
                socket.send( sendPacket );
                output.setCaretPosition(output.getText().length());
                input.setText("");
            }
            catch ( IOException ioException ) {
                displayMessage( ioException.toString() + "\n" );
                ioException.printStackTrace();
            }
            
        }
        catch ( SocketException socketException ) {
            socketException.printStackTrace();
            System.exit( 1 );
        }
    }

    // espera que os pacotes cheguem do Server, exibe o conteudo do pacote
    public void waitForPackets() {
        while ( true ) {
            // recebe o pacote e exibe o conteudo
            try {
                // configura o pacote
                byte data[] = new byte[ 100 ];
                DatagramPacket receivePacket = new DatagramPacket(data, data.length );                           
                // espera o pacote
                socket.receive( receivePacket );
                // exibe o conte�do do pacote
                displayMessage(new String(receivePacket.getData(), 0, receivePacket.getLength())+"\n");
            }
            catch ( IOException exception ) {
                displayMessage( exception.toString() + "\n" );
                exception.printStackTrace();
            }
        }
    }

    // manipula a displayArea na thread de despacho de eventos
    private void displayMessage( final String messageToDisplay ) {
        SwingUtilities.invokeLater(
            new Runnable() {
                // atualiza a displayArea 
                public void run() {
                   output.append( messageToDisplay );
                }
            }
        );
    }
    
}