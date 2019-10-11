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
    // private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private byte tempBuffer[] = new byte[packetsize];
    // private static long sequenceNo=0;
    // private boolean stopCapture = false;


    public Sender (InetAddress IP,int port){
        this.MulticastIP = IP;
        this.Port = port;
    }

    private void sendPacket( MulticastSocket socket ){
        // byte[] newByte;
        // PacketData pd;
        // stopCapture = false;

        try{
            int readCount;
            // while (!stopCapture) {
                readCount = targetDataLine.read(tempBuffer, 0, tempBuffer.length);  //capture sound into tempBuffer

                if (readCount > 0) {
                    // ++sequenceNo;
                    // sequenceNo=sequenceNo%Integer.MAX_VALUE;
                    // //creating packet with a sequence NUmber
                    // pd=new PacketData( sequenceNo, tempBuffer );
                    // newByte=PacketData.fullByteArray(pd);
                    
                    // DatagramPacket packet =new DatagramPacket( newByte, newByte.length, MulticastIP, Port ); 

                    //NEW*************** 
                    DatagramPacket packet =new DatagramPacket( tempBuffer, tempBuffer.length, MulticastIP, Port );
                     //NEW***************

                     // Send the packet
                    socket.setTimeToLive(2);
                    socket.send( packet ) ;
                    socket.setLoopbackMode(true);
                }
            // }
        }catch( IOException e ) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run(){
        try{
            socket = new MulticastSocket(); 
            Audio audioObj = new Audio(1);
            targetDataLine = audioObj.getTargetDataLine();
            sendPacket( socket );

        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }finally{
            socket.close() ;
        }
    }
}
