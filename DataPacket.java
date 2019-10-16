import java.io.*;
import java.io.Serializable;

public class DataPacket implements Serializable{
	
	private static final long serialVersionUID = -6470090944414208496L;
	private byte [] voiceBuffer;
	private long sequenceNo;
	// private int id;

	public DataPacket( byte [] voice, long sequenceNo, int id ){
		this.voiceBuffer = voice;		
		this.sequenceNo = sequenceNo;
		// this.id = id;
	}
     
	public static DataPacket ByteArrayToObject(byte [] data) throws IOException{
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