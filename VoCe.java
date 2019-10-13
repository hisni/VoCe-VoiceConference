import java.net.* ;

public class VoCe{
	public static void main(String args[]){
		if( args.length == 1 ){

            InetAddress multicastIP;
            int port;
            try{
                multicastIP = InetAddress.getByName( args[0] ) ;
                port = Integer.parseInt( "9000") ;

                Audio audioObj = new Audio();
                
                Receiver receiver = new Receiver( multicastIP, port, audioObj );
                receiver.start();
                Sender  sender = new Sender( multicastIP, port, audioObj );
                sender.start();
                Player player = new Player( audioObj );
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