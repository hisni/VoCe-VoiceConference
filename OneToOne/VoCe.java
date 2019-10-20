import java.net.InetAddress;

public class VoCe{
	public static void main(String args[]){
		if( args.length == 1 ){

            InetAddress destIP;
            int port;

            try{
                destIP = InetAddress.getByName( args[0] ) ;
                port = Integer.parseInt( "35001") ;

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
            System.out.println( "USAGE: java VoCe <DestinationIP>" ) ;
            return;
		}
    }
}