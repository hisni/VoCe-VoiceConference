import java.net.* ;
import java.net.MulticastSocket;

public class Receiver extends Thread{

    public final static int packetsize = 594 ;

    private int Port;
	private DatagramSocket socket;
    
    public Receiver( int port){
		this.Port=port;
    }
	public void run (){
		try{
			socket = new DatagramSocket( Port );	//Construct the socket
			DataPacket dataPacket;

			while( true ){
				DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize );

				socket.receive( packet );			//Receive a packet (blocking)
				dataPacket = DataPacket.ByteArrayToObject( packet.getData() );
				Player.addToBuffer( dataPacket );
			}    
		}catch( Exception e ){
			System.out.println( e ) ;
			e.printStackTrace();
		}finally{
			socket.close();
		}
	}
}
