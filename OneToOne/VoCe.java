import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class VoCe{
	public static void main(String args[]){
		if( args.length == 1 ){

            InetAddress destIP;
            int port;

            try{
                destIP = InetAddress.getByName( args[0] ) ;
                port = Integer.parseInt( "9005") ;

                Audio audioObj = new Audio();
                
                Receiver receiver = new Receiver( port, audioObj );
                Sender  sender = new Sender( destIP, port, audioObj );
                receiver.start();
                sender.start();
                
            }catch(Exception e){
                System.out.println(e);
                e.printStackTrace();
            }
        }
        else{
            System.out.println( "usage: java Control <Destination IP>" ) ;
            return;
		}
    }
}