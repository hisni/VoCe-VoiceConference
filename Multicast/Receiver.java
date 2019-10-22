import java.net.* ;
import java.net.MulticastSocket;

public class Receiver extends Thread{

    public final static int packetsize = VoCe.PACKET_SIZE + 103 ;

    private InetAddress MulticastIP;
    private int Port;
	private MulticastSocket socket;

	private int currUser;
	private int prevUser;
    
    public Receiver( InetAddress IP, int port){
        this.MulticastIP=IP;
		this.Port=port;
	}

	public void run (){
		try{
			//get the network interface
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress( VoCe.getLocalAddress() );
			
			socket = new MulticastSocket( Port );	//Construct the Multicast socket and bind it to a port (Receiving port)
			socket.setNetworkInterface( NetworkInterface.getByName(networkInterface.getName()) );	//Set the network interface
			socket.setBroadcast(true);				//Enable multicast for windows
			socket.joinGroup( MulticastIP );		//Join Multicast group
			socket.setLoopbackMode(true);			//Disable Loopback
			
			DataPacket dataPacket;
			DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize );
			socket.receive( packet );
			dataPacket = DataPacket.ByteArrayToObject( packet.getData() );
			Player.addToBuffer( dataPacket );
			prevUser = dataPacket.getId();
			
			while( true ){			//Continuously receive packets and add to buffer
				packet = new DatagramPacket( new byte[packetsize], packetsize );

				socket.receive( packet );		//Receive a packet
				dataPacket = DataPacket.ByteArrayToObject( packet.getData() ); //Convert byte array to object
				currUser = dataPacket.getId();

				if( prevUser == currUser ){
					Player.addToBuffer( dataPacket );		//Add the received packet to buffer
				}else{
					Player.changeUser();
					prevUser = currUser;
					Player.addToBuffer( dataPacket );		//Add the received packet to buffer
				}
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
