import java.net.*;
import java.util.*;

public class VoCe extends Thread{

    public static int PACKET_SIZE = 512;
    private static InetAddress multicastIP;
    private static int port;

    public static InetAddress getLocalAddress(){
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
	public static void main(String args[]){
		if( args.length == 1 ){

            int userID = getLocalAddress().hashCode();

            try{
                multicastIP = InetAddress.getByName( args[0] ) ;
                port = Integer.parseInt( "8003") ;

                Audio audioObj = new Audio();
                
                Interaction intObj = new Interaction(0);
                Receiver receiver = new Receiver( multicastIP, port );
                Sender sender = new Sender( multicastIP, port, userID, audioObj );
                Player player = new Player( audioObj );
                intObj.start();
                receiver.start();
                sender.start();
                player.start();

            }catch(Exception e){
                System.out.println(e);
                e.printStackTrace();
            }
        }
        else{
            System.out.println( "usage: java Control <Multicast IP>" ) ;
            return;
		}
    }
    
}