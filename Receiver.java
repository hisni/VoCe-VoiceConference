import java.net.* ;
import java.util.*;
// import java.io.IOException; 
// import java.util.Date;
import java.net.MulticastSocket;


public class Receiver extends Thread{

    public final static int packetsize = 1003 ;

    // private byte tempBuffer[];
    private InetAddress MulticastIP;
    private int Port;
	private MulticastSocket socket;
    private Audio audioObj;

    public Receiver( InetAddress IP, int port, Audio audioObj ){
        this.MulticastIP=IP;
		this.Port=port;
        this.audioObj = audioObj;		
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
        // long exp_pkt_no = 0;
		// long loss = 0;
		// long StartTime,EndTime,diff; 

		try{
			//get the network interface
			NetworkInterface ni = NetworkInterface.getByInetAddress( getLocalAddress() );
			
			// Construct the socket
			socket = new MulticastSocket( Port );//non-recoverable
			socket.setNetworkInterface( NetworkInterface.getByName(ni.getName()) );//set the network interface
			socket.joinGroup( MulticastIP );

			// System.out.println( "The server is ready..." ) ;
			// //Error correction
			// StartTime = new Date().getTime();
			// ErrCorrection ec 	 = new ErrCorrection(); 
			// Statistics details = new Statistics();

			while( true ){
				try{
					DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize ) ;//non-recoverable
			// 		// Receive a packet (blocking)
					// socket.setLoopbackMode(true);
					socket.receive( packet ) ;
					DataPacket dataPacket = new DataPacket( packet.getData() );

			// 		ec.addPckt( dataPacket.getSequenceNo(), dataPacket.getId() );//log packet info
			// 		EndTime = new Date().getTime();	

			// 		if( (EndTime - StartTime) > 60000 ){//reset log every 60 sec
			// 			System.out.println("Packets expected in the previous minute : "+ec.expectedPkts());
			// 			System.out.println("Packets lost in the previous minute : "+ec.getLoss());
			// 			System.out.println("Packets unordered in the previous minute : "+ec.getUnorderedPkts());
			// 			System.out.println("***********************************************");
								
			// 			StartTime = new Date().getTime(); // reset timer
			// 			details.update(ec);

			// 			System.out.println("Packet loss so far : "+details.getTotalLoss());
			// 			System.out.println("Packet unordered so far : "+details.getTotalUOrdered());
			// 			System.out.println("Approximate time since start : "+details.getApproxTime());
			// 			ec = new ErrCorrection();
			//		}
					// System.out.println( dataPacket.getSequenceNo() );
					audioObj.playAudio( dataPacket.getVoice() );
					// audioObj.playAudio( packet.getData() );
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
