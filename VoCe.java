import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class VoCe{
	public static void main(String args[]){
		if( args.length == 1 ){

            InetAddress multicastIP;
            int port;
            int userID = getLocalAddress().hashCode();

            try{
                multicastIP = InetAddress.getByName( args[0] ) ;
                port = Integer.parseInt( "9003") ;

                Audio audioObj = new Audio();
                
                Receiver receiver = new Receiver( multicastIP, port );
                Sender  sender = new Sender( multicastIP, port, userID, audioObj );
                Player player = new Player( audioObj );
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
    
    public static InetAddress getLocalAddress() {
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
}