import java.net.* ;
// import java.util.*;
import java.io.IOException;
import javax.sound.sampled.TargetDataLine;
// import javax.sound.sampled.AudioFormat;
import java.net.MulticastSocket;

public class Sender extends Thread {

    public final static int packetsize = 900;  

    private MulticastSocket socket = null;
    private InetAddress MulticastIP;
    private int Port;
    private TargetDataLine targetDataLine;
    private byte tempBuffer[] = new byte[packetsize];
    private static long sequenceNo=0;
    private boolean stopCapture = false;
    private Audio audioObj;

    public Sender( InetAddress IP, int port, Audio audioObj ){
        this.MulticastIP = IP;
        this.Port = port;
        this.audioObj = audioObj;
    }

    private void sendPacket(){
        stopCapture = false;

        try{
            int readCount;
            while (!stopCapture) {
                readCount = targetDataLine.read(tempBuffer, 0, tempBuffer.length);  //capture sound into tempBuffer

                if ( readCount > 0 ) {
                    ++sequenceNo;
                    sequenceNo = sequenceNo%Integer.MAX_VALUE;
                    //creating packet with a sequence NUmber
                    DataPacket dataPacket = new DataPacket( sequenceNo, tempBuffer );
                    // byte[] newBuffer = DataPacket.serialize( dataPacket );
                    byte[] newBuffer = DataPacket.ObjectToByteArray( dataPacket );
                    
                    DatagramPacket packet = new DatagramPacket( newBuffer, newBuffer.length, MulticastIP, Port ); 

                    // Send the packet
                    socket.setTimeToLive(2);
                    socket.send( packet );
                    socket.setLoopbackMode(true);
                }
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
            targetDataLine = audioObj.getTargetDataLine();
            sendPacket();

        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();

        }finally{
            socket.close();

        }
    }
}
