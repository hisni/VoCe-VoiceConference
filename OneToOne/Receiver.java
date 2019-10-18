import java.net.* ;
import java.net.MulticastSocket;

public class Receiver extends Thread{

    public final static int packetsize = 594 ;

    private int Port;
	private DatagramSocket socket;
	private Audio audioObj;
    
    public Receiver( int port, Audio audioObj){
		this.Port = port;
		this.audioObj = audioObj; 
    }
	public void run (){
		try{
			socket = new DatagramSocket( Port );	//Construct the socket
			DataPacket dataPacket;

			while( true ){
				DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize );

				socket.receive( packet );			//Receive a packet (blocking)
				dataPacket = DataPacket.ByteArrayToObject( packet.getData() );
				System.out.println(dataPacket.getSequenceNo());
				audioObj.playAudio( dataPacket.getVoice() );
			}    
		}catch( Exception e ){
			System.out.println( e ) ;
			e.printStackTrace();
		}finally{
			socket.close();
		}
	}
}
