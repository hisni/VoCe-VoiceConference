
public class Player extends Thread {

    static volatile long startTime = (long)System.currentTimeMillis();

    private Audio audioObj;
    private static DataPacket Buffer[] = new DataPacket[1024];
    
    private static int threshold = 32;
    private static long currentPlaying = -1;
    private static long receivedPackets = 0;
    private static long lossedPackets = 0;
    private static long lossedPacketsCount = 0;
    private static long unorderedPacket = 0;

    public Player( Audio audioObj ){
        this.audioObj = audioObj;
    }

    public static void addToBuffer( DataPacket dp ){
        receivedPackets++;
        if( dp.getSequenceNo() > currentPlaying ){
            Buffer [(int)(dp.getSequenceNo()%1024)] = dp;
        }else{
            unorderedPacket++;
        }
    }

    private int getIndex( int i ){
        return (int)( ((currentPlaying%1024)+i+1)%1024 );
    }

    private DataPacket getAudio(){
        
        while(true){
			int counter=0;
			for( int i=0; i<1024; i++){
				if( Buffer[ getIndex(i) ] != null ) counter++;
			}     
			if( counter > threshold )break;    
	    }
        
        DataPacket dataPacket;

        for(int j=0;j<1024;j++){
            if( Buffer[ getIndex(j) ] != null){
                long sequenceNo = Buffer[ getIndex(j) ].getSequenceNo();
                
                if( sequenceNo >= currentPlaying ){
                    dataPacket =  Buffer[ getIndex(j) ];
                    Buffer[ getIndex(j) ] = null;
                    return dataPacket;
                }
                else{
                    Buffer[ getIndex(j) ] = null;
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

            if( (long)System.currentTimeMillis() > startTime + 10000 ){
                lossedPackets = dp.getSequenceNo() - receivedPackets - lossedPacketsCount;
                lossedPacketsCount += lossedPackets;
                System.out.println("Packet Size: 900 | Packet Loss: " + lossedPackets + " Unorderd Packets: " + unorderedPacket );
                startTime = System.currentTimeMillis();
                unorderedPacket = 0;
                
            }
        }
    }

}