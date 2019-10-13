
public class Player extends Thread {

    static volatile long startTime = (long)System.currentTimeMillis();

    private Audio audioObj;
    private static DataPacket Buffer[] = new DataPacket[1024];
    
    // private int curr_sending = 0;
    private static int threshold = 32;
    private static long currentPlaying = -1;
    public static long unorderedPacket=0;

    public Player( Audio audioObj ){
        this.audioObj = audioObj;
    }

    public static void addToBuffer( DataPacket dp ){
        if( dp.getSequenceNo() > currentPlaying ){
            Buffer [(int)(dp.getSequenceNo()%1024)] = dp;
        }else{
            unorderedPacket++;
        }

        if( (long)System.currentTimeMillis() > startTime + 10000 ){
			System.out.println("Packet Size: 900 | Unorderd Packets: " + unorderedPacket );
			startTime = System.currentTimeMillis();
            unorderedPacket = 0;
		}
    }

    private DataPacket getAudio(){
        
        while(true){
			int counter=0;
			for( int i=0; i<1024; i++){
				if( Buffer[i] != null ) counter++;
			}     
			if( counter > threshold )break;      
	    }
        
        DataPacket dataPacket;

        for(int j=0;j<1024;j++){
            if( Buffer[j] != null){
                long sequenceNo = Buffer[j].getSequenceNo();
                
                if( sequenceNo >= currentPlaying ){
                    dataPacket =  Buffer[j];
                    Buffer[j] = null;
                    return dataPacket;
                }
                else{
                    Buffer[j] = null;
                }
            }
        }
        
        return null;
    }

    public void run (){
        while(true){
            DataPacket dataPacket = getAudio();
            if( dataPacket != null ){
                currentPlaying = dataPacket.getSequenceNo();
                audioObj.playAudio( dataPacket.getVoice() );
            }
        }
    }

}