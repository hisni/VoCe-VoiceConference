import java.io.*;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.nio.ByteBuffer;

public class DataPacket implements Serializable{
	
	private static final long serialVersionUID = -6470090944414208496L;
	private byte [] voiceBuffer;
	private long sequenceNo;
	private int id;

	public DataPacket( long sequenceNo, byte [] voice ){
		this.voiceBuffer = voice;		
		this.sequenceNo = sequenceNo;
		this.id = 10;
	}

	public DataPacket( byte [] data ) throws IOException, ClassNotFoundException {
		DataPacket tempPacket = ByteArrayToObject( data );
		this.voiceBuffer = tempPacket.voiceBuffer;
		this.sequenceNo  = tempPacket.sequenceNo;
		this.id  = tempPacket.id; 
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
     
	public DataPacket ByteArrayToObject(byte [] data) throws IOException{
		ObjectInputStream  ois = null;
		try{
			ByteArrayInputStream b = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(b);

			return (DataPacket)ois.readObject();			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			ois.close();
		}
	}

	public static byte[] ObjectToByteArray( DataPacket packet ) throws IOException  {
		ObjectOutputStream  oos = null;
		try{  
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(buffer);
			oos.writeObject(packet);
			oos.flush();

			return buffer.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			oos.close();
		}
	}

	public long getSequenceNo(){
		return this.sequenceNo;
	}

	public byte[] getVoice(){
            return voiceBuffer;
	}

	// public int getId(){
	// 	return this.id;
	// }
	
}