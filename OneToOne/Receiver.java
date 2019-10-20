import java.net.* ;

public class Receiver extends Thread{

    private final static int packetsize = VoCe.PACKET_SIZE;

    private int Port;
	private DatagramSocket socket;
	private Audio audioObj;
    
    public Receiver( int port, Audio audioObj){
		this.Port = port;
		this.audioObj = audioObj; 
	}
	
	public void run (){
		try{
			socket = new DatagramSocket( Port );	//Construct the socket and bind to a port

			while( true ){			//Continuously receive packets and play
				DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize );

				socket.receive( packet );			//Receive a packet
				audioObj.playAudio( packet.getData() );
			}

		}catch( Exception e ){
			System.out.println( e ) ;
			e.printStackTrace();
		}finally{
			socket.close();
		}
	}
}
