import java.net.* ;

public class Sender extends Thread {

    private final int packetsize = VoCe.PACKET_SIZE;

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

    public void run(){
        try{
            socket = new DatagramSocket();  //Construct Datagram Socket
            
            while ( true ){             //Continuously capture audio and send
                buffer = audioObj.captureAudio();   //Capture audio

                DatagramPacket packet = new DatagramPacket( buffer, buffer.length, destIP, Port );  //Create a Datagram packet
                socket.send( packet ); //Send the packet 
            }

        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }finally{
            socket.close();
        }
    }
}
