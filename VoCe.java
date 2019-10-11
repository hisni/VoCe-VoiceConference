import java.net.* ;

public class VoCe{
	
	public static void main(String args[]){
		if( args.length == 1 ){

            InetAddress multicastIP;
            int port;
            try{
                multicastIP = InetAddress.getByName( args[0] ) ;
                port = Integer.parseInt( "9000") ;
                
                Receiver receiver = new Receiver( multicastIP, port );
                receiver.start();
                Sender  sender = new Sender( multicastIP, port );
                sender.start();

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