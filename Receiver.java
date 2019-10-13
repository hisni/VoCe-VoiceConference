import java.net.* ;
import java.util.*;
// import java.io.IOException; 
// import java.util.Date;
import java.net.MulticastSocket;

public class Receiver extends Thread{

    public final static int packetsize = 594 ;

    // private byte tempBuffer[];
    private InetAddress MulticastIP;
    private int Port;
	private MulticastSocket socket;
    // private Audio audioObj;

    public Receiver( InetAddress IP, int port, Audio audioObj ){
        this.MulticastIP=IP;
		this.Port=port;
        // this.audioObj = audioObj;		
    }

	public InetAddress getLocalAddress() {
		try{
		    Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
		    while( ifaces.hasMoreElements() ){
		      	NetworkInterface iface = ifaces.nextElement();
		     	Enumeration<InetAddress> addresses = iface.getInetAddresses();

			    while( addresses.hasMoreElements() ){
			        InetAddress addr = addresses.nextElement();
			        if( addr instanceof Inet4Address && !addr.isLoopbackAddress() ){
			          return addr;
			        }
		      	}
		    }
		    return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void run (){
		try{
			//get the network interface
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress( getLocalAddress() );
			// Construct the socket
			socket = new MulticastSocket( Port );	//non-recoverable
			socket.setNetworkInterface( NetworkInterface.getByName(networkInterface.getName()) );//set the network interface
			socket.joinGroup( MulticastIP );
			// socket.setLoopbackMode(true);
			
			DataPacket dataPacket;

			while( true ){
				try{
					DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize );	//non-recoverable
	
					socket.receive( packet );		//Receive a packet (blocking)
					dataPacket = new DataPacket( packet.getData() );
					Player.addToBuffer( dataPacket );
					
				}catch(Exception e){
					System.out.println(e);
					e.printStackTrace();
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
