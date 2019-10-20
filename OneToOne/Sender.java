import java.net.* ;
import java.io.IOException;

public class Sender extends Thread {

    public final static int packetsize = 500;  

    private DatagramSocket socket = null;
    private InetAddress destIP;
    private int Port;
    private byte buffer[] = new byte[packetsize];
    private Audio audioObj;

    public Sender( InetAddress IP, int port, Audio audioObj ){
        this.destIP = IP;
        this.Port = port;
        this.audioObj = audioObj;
    }

    private void sendPacket(){
        try{           
            while ( true ){             //Continuously capture audio and send
                buffer = audioObj.captureAudio();   //Capture

                DatagramPacket packet = new DatagramPacket( buffer, buffer.length, destIP, Port );  //Create Datagram packet
                socket.send( packet ); //Send the packet 
            }
        }catch( IOException e ) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            socket = new DatagramSocket();  //Construct Datagram Socket
            sendPacket();                   //Capture and send Packet
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }finally{
            socket.close();
        }
    }
}
