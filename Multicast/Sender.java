import java.net.* ;
import java.io.IOException;
import java.net.MulticastSocket;

public class Sender extends Thread {

    public final static int packetsize = VoCe.PACKET_SIZE;  

    private MulticastSocket socket = null;
    private InetAddress MulticastIP;
    private int Port;
    private int userID;
    private byte tempBuffer[] = new byte[packetsize];
    private static long sequenceNo=0;
    private Audio audioObj;

    public Sender( InetAddress IP, int port, int id ,Audio audioObj ){
        this.MulticastIP = IP;
        this.Port = port;
        this.userID = id;
        this.audioObj = audioObj;
    }

    private void sendPacket(){
        DataPacket dataPacket;
        byte[] buffer;

        try{           
            while( true ) {
                tempBuffer = audioObj.captureAudio( packetsize );
                
                if( Interaction.getCurrState() == 1 ){
                    sequenceNo = ( sequenceNo + 1 )%Integer.MAX_VALUE;
                    dataPacket = new DataPacket( tempBuffer, sequenceNo, userID );

                    buffer = DataPacket.ObjectToByteArray( dataPacket );
                    DatagramPacket packet = new DatagramPacket( buffer, buffer.length, MulticastIP, Port ); 
                    
                    socket.setTimeToLive(2);
                    socket.send( packet );
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
