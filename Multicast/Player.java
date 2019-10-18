
public class Player extends Thread {

    static volatile long startTime = (long)System.currentTimeMillis();

    private static final int BUFFSIZE = 256;
    private static int THRESHOLD = 32;
    
    private static DataPacket Buffer[][] = new DataPacket[2][BUFFSIZE];
    private static long currentPlaying = -1;
    private static long unorderedPacket = 0;
    private static long receivedPackets = 0;
    private static long currReceivedSeq = 0;
    private static long prevReceivedSeq = 0;
    private long packetLoss = 0;
    private long totalPacketLoss = 0;
    private Audio audioObj;
    private int ID = 0;

    public Player( Audio audioObj ){
        this.audioObj = audioObj;
    }

    public static void addToBuffer( DataPacket dp, int ID ){
        if( dp.getSequenceNo() > currentPlaying ){
            Buffer[ID][(int)(dp.getSequenceNo()%BUFFSIZE)] = dp;
        }
        currReceivedSeq = dp.getSequenceNo();
        if( currReceivedSeq < prevReceivedSeq ){
            unorderedPacket++;
        }
        prevReceivedSeq = currReceivedSeq;
        receivedPackets++;
    }

    private DataPacket getAudio(){
        
        while(true){
			int counter=0;
			for( int i=0; i<BUFFSIZE; i++){
				if( Buffer[ID][ getIndex(i) ] != null ) counter++;
			}     
			if( counter > THRESHOLD )break;      
	    }
        
        DataPacket dataPacket;

        for(int j=0;j<BUFFSIZE;j++){
            if( Buffer[ID][ getIndex(j) ] != null){
                long sequenceNo = Buffer[ID][ getIndex(j) ].getSequenceNo();
                
                if( sequenceNo >= currentPlaying ){
                    dataPacket =  Buffer[ID][ getIndex(j) ];
                    Buffer[ID][ getIndex(j) ] = null;
                    return dataPacket;
                }
                else{
                    Buffer[ID][ getIndex(j) ] = null;
                }
            }
        }
        return null;
    }

    private int getIndex( int i ){
        return (int)( ((currentPlaying%BUFFSIZE) + i + 1)%BUFFSIZE );
    }

    public long getUnordered(){
        return unorderedPacket;
    }

    public void resetStats(){
        unorderedPacket = 0;
        packetLoss = 0;
    }

    public long getPacketLoss(){
        packetLoss = (currReceivedSeq - receivedPackets) - totalPacketLoss;
        return packetLoss;
    }

    public void run (){
        while(true){
            DataPacket dataPacket = getAudio();
            if( dataPacket != null ){
                currentPlaying = dataPacket.getSequenceNo();
                audioObj.playAudio( dataPacket.getVoice() );
            }

            if( (long)System.currentTimeMillis() > startTime + 10000 ){
                System.out.println("Packet Size: 900 | PacketLoss: " + getPacketLoss() + " | Unorderd Packets: " + getUnordered() );
                startTime = System.currentTimeMillis();
                resetStats();
            }
        }
    }

}