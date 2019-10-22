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
            //Continuously run and check user state
            //If user wants to speak ( state=1 ) capture audio and send the packet
            //Else keep waiting (listening only state)
            while( true ) {
                tempBuffer = audioObj.captureAudio( packetsize );
                
                if( Interaction.getCurrState() == 1 ){
                    sequenceNo = ( sequenceNo + 1 )%Integer.MAX_VALUE;  //Add sequence number
                    dataPacket = new DataPacket( tempBuffer, sequenceNo, userID );  //Create a data packet object

                    buffer = DataPacket.ObjectToByteArray( dataPacket );    //Convert Data packet object to byte array
                    DatagramPacket packet = new DatagramPacket( buffer, buffer.length, MulticastIP, Port ); //Convert object to byte array
                    
                    socket.setTimeToLive(2);    //Set TTL
                    socket.send( packet );      //Send the Packet
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
            socket = new MulticastSocket(); //Create a multicast socket
            socket.setBroadcast(true);      //Enable multicast for windows
            socket.setLoopbackMode(true);   //Disable loopback
            sendPacket();                   //Send packets according to the user state
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }finally{
            socket.close();
        }
    }
}
