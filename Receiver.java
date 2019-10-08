import java.net.* ;
import java.util.*;
import java.io.IOException; 
import java.util.Date;
import java.net.MulticastSocket;


public class Receiver extends Thread{

    // public final static int packetsize = 1000 ;

    // private byte tempBuffer[];
    private InetAddress MulticastIP;
    private int Port;
    // private MulticastSocket socket;

    public Receiver( InetAddress IP, int port ){
        this.MulticastIP=IP;
        this.Port=port;
    }

	public void run (){
        System.out.println("Receiver" + MulticastIP + " " + Port);
		// long exp_pkt_no = 0;
		// long loss = 0;
		// long StartTime,EndTime,diff; 

		// try{
		// 	//get the network interface
		// 	NetworkInterface ni = NetworkInterface.getByInetAddress(new PacketData(0,null).getLocalAddress());
			

		// 	// Construct the socket
		// 	socket = new MulticastSocket( port ) ;//non-recoverable
		// 	socket.setNetworkInterface(NetworkInterface.getByName(ni.getName()));//set the network interface
		// 	socket.joinGroup(host);

		// 	System.out.println( "The server is ready..." ) ;
		// 	PacketData pd;


		// 	//Error correction
		// 	StartTime = new Date().getTime();
		// 	ErrCorrection ec 	 = new ErrCorrection(); 
		// 	Statistics details = new Statistics();

		// 	RecoderAndPlay recPlay = new RecoderAndPlay(2);

		// 	for( ;; ){
		// 		try{
		// 			DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize ) ;//non-recoverable
		// 			// Receive a packet (blocking)
		// 			socket.receive( packet ) ;


		// 			pd = new PacketData(packet.getData());

		// 			ec.addPckt(pd.getSequenceNo(), pd.getId());//log packet info
				
		// 			EndTime = new Date().getTime();	

		// 			if( (EndTime - StartTime) > 60000 ){//reset log every 60 sec
		// 				System.out.println("Packets expected in the previous minute : "+ec.expectedPkts());
		// 				System.out.println("Packets lost in the previous minute : "+ec.getLoss());
		// 				System.out.println("Packets unordered in the previous minute : "+ec.getUnorderedPkts());
		// 				System.out.println("***********************************************");
								
		// 				StartTime = new Date().getTime(); // reset timer
		// 				details.update(ec);

		// 				System.out.println("Packet loss so far : "+details.getTotalLoss());
		// 				System.out.println("Packet unordered so far : "+details.getTotalUOrdered());
		// 				System.out.println("Approximate time since start : "+details.getApproxTime());
		// 				ec = new ErrCorrection();
		// 			}
		// 			recPlay.player(pd.getVoice());
			
		// 		}catch(Exception e){
		// 			System.out.println(e);
		// 			e.printStackTrace();
		// 		}
		// 	}    
		// }catch( Exception e ){
		// 	System.out.println( e ) ;
		// 	e.printStackTrace();
		// }finally{
		// 	try{
		// 	socket.leaveGroup(host);
		// 	}catch(Exception e){
		// 		e.printStackTrace();
		// 	}
		// 	socket.close();
		// }
	}
}
