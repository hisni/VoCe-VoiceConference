import java.io.*;
import java.io.Serializable;
import java.util.Arrays;

public class DataPacket implements Serializable{
	
	private static final long serialVersionUID = -6470090944414208496L;
	private byte [] voiceBuffer;
	private long sequenceNo;
	private int id;

	public DataPacket( byte [] voice, long sequenceNo, int id ){
		this.voiceBuffer = voice;		
		this.sequenceNo = sequenceNo;
		this.id = id;
	}
	 
	//Method to convert object to byte array
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

	//Method to convert byte array to object
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

	//Method get sequence number
	public long getSequenceNo(){
		return this.sequenceNo;
	}

	//Method set sequence number
	public void setSequenceNo( long seq ){
		this.sequenceNo = seq;
	}

	//Method get voice buffer
	public byte[] getVoice(){
		return voiceBuffer;
	}

	//Method get user id
	public int getId(){
		return this.id;
	}

	@Override
   	public boolean equals( Object obj ){
		DataPacket pd = (DataPacket)obj;
		if( Arrays.equals( this.getVoice(), pd.getVoice() ) && this.getId() == pd.getId() && this.getSequenceNo() == pd.getSequenceNo()){
			return true;
		}
		return false;
	}
	
}