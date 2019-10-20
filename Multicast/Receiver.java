import java.net.* ;
import java.net.MulticastSocket;

public class Receiver extends Thread{

    public final static int packetsize = VoCe.PACKET_SIZE + 103 ;

    private InetAddress MulticastIP;
    private int Port;
	private MulticastSocket socket;
    
    public Receiver( InetAddress IP, int port){
        this.MulticastIP=IP;
		this.Port=port;
    }

	public void run (){
		try{
			//get the network interface
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress( VoCe.getLocalAddress() );
			// Construct the socket
			socket = new MulticastSocket( Port );
			socket.setNetworkInterface( NetworkInterface.getByName(networkInterface.getName()) );//set the network interface
			socket.joinGroup( MulticastIP );
			socket.setLoopbackMode(true);
			
			DataPacket dataPacket;

			while( true ){
				DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize );

				socket.receive( packet );		//Receive a packet (blocking)

				dataPacket = DataPacket.ByteArrayToObject( packet.getData() );
				Player.addToBuffer( dataPacket, 0 );
			}    
		}catch( Exception e ){
			System.out.println( e ) ;
			e.printStackTrace();
		}finally{
			try{
				socket.leaveGroup(MulticastIP);
			}catch(Exception e){
				e.printStackTrace();
			}
			socket.close();
		}
	}
}
