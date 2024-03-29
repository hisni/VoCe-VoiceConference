import java.net.*;
import java.util.*;

public class VoCe extends Thread{

    public static int PACKET_SIZE = 512;
    private static InetAddress multicastIP;
    private static int port;

    public static InetAddress getLocalAddress(){        //Get Local IP Address
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
            int userID = getLocalAddress().hashCode();  //Get hash value of local IP address as User ID

            try{
                multicastIP = InetAddress.getByName( args[0] );
                port = Integer.parseInt( "25000") ;

                Audio audioObj = new Audio();                
                //Create sperate threads for send, receive, play audio and interact with user and start the treads
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
            System.out.println( "USAGE: java VoCe <MulticastIP>" ) ;
            return;
		}
    }
    
}