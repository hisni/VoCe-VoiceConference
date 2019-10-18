import java.net.* ;
import java.io.IOException;
import java.net.MulticastSocket;

public class Sender extends Thread {

    public final static int packetsize = 500;  

    private DatagramSocket socket = null;
    private InetAddress destIP;
    private int Port;
    private byte tempBuffer[] = new byte[packetsize];
    private static long sequenceNo=0;
    private Audio audioObj;

    public Sender( InetAddress IP, int port, Audio audioObj ){
        this.destIP = IP;
        this.Port = port;
        this.audioObj = audioObj;
    }

    private void sendPacket(){
        DataPacket dataPacket;
        byte[] buffer;

        try{           
            while ( true ) {
                tempBuffer = audioObj.captureAudio();

                sequenceNo = ( sequenceNo + 1 )%Integer.MAX_VALUE;
                dataPacket = new DataPacket( tempBuffer, sequenceNo );

                buffer = DataPacket.ObjectToByteArray( dataPacket );
                DatagramPacket packet = new DatagramPacket( buffer, buffer.length, destIP, Port ); 
                System.out.println( "Sender "+ dataPacket.getSequenceNo() );
                socket.send( packet );  //Send the packet
            }
        }catch( IOException e ) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run(){
        try{
            socket = new DatagramSocket();
            sendPacket();
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }finally{
            socket.close();
        }
    }
}
