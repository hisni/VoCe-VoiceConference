import java.net.* ;
import java.io.IOException;
import java.net.MulticastSocket;

public class Sender extends Thread {

    public final static int packetsize = 500;  

    private MulticastSocket socket = null;
    private InetAddress MulticastIP;
    private int Port;
    private byte tempBuffer[] = new byte[packetsize];
    private static long sequenceNo=0;
    // private boolean stopCapture = false;
    private Audio audioObj;

    public Sender( InetAddress IP, int port, Audio audioObj ){
        this.MulticastIP = IP;
        this.Port = port;
        this.audioObj = audioObj;
    }

    private void sendPacket(){
        DataPacket dataPacket;
        byte[] newBuffer;

        try{           
            while ( true ) {
                tempBuffer = audioObj.captureAudio();

                sequenceNo = (sequenceNo + 1 )%Integer.MAX_VALUE;
                dataPacket = new DataPacket( sequenceNo, tempBuffer );

                newBuffer = DataPacket.ObjectToByteArray( dataPacket );
                
                DatagramPacket packet = new DatagramPacket( newBuffer, newBuffer.length, MulticastIP, Port ); 
                
                // Send the packet
                socket.setTimeToLive(2);
                socket.send( packet );
    
            }
        }catch( IOException e ) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run(){
        try{
            socket = new MulticastSocket();
            socket.setLoopbackMode(true);
            sendPacket();
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }finally{
            socket.close();
        }
    }
}
