public class Player extends Thread {

    static volatile long startTime = (long)System.currentTimeMillis();

    private static final int BUFFSIZE = 512;
    private static int THRESHOLD = 32;
    
    private static DataPacket Buffer[] = new DataPacket[BUFFSIZE];
    private static long currentPlaying = -1;
    private static long unorderedPacket = 0;
    private static long prevPacket = 0;
    private static long intervalPackets = 0;
    private static long currReceivedSeq = 0;
    private static long prevReceivedSeq = -1;
    private long packetLoss = 0;
    private Audio audioObj;

    public Player( Audio audioObj ){
        this.audioObj = audioObj;
    }

    public static void addToBuffer( DataPacket dp ){        //Add receiving packet to buffer
        if( dp.getSequenceNo() > currentPlaying ){
            dp.setSequenceNo( dp.getSequenceNo() );
            Buffer[(int)(dp.getSequenceNo()%BUFFSIZE)] = dp;
        }
        currReceivedSeq = dp.getSequenceNo();
        if( currReceivedSeq < prevReceivedSeq ){
            unorderedPacket++;
        }
        // else{
            prevReceivedSeq = currReceivedSeq;
        // }
        intervalPackets++;
    }

    private DataPacket getAudio(){  //Get a packet from buffer
        
        while( true ){              //Check whether buffer is filled enough
			int counter = 0;
			for( int i=0; i<BUFFSIZE; i++){
				if( Buffer[ getIndex(i) ] != null ) counter++;
			}     
			if( counter > THRESHOLD )break;      
	    }
        
        DataPacket dataPacket;

        for( int j=0; j<BUFFSIZE; j++ ){
            if( Buffer[ getIndex(j) ] != null ){
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

    public static void changeUser(){
        currentPlaying = -1;
        prevReceivedSeq = -1;
    }

    // Method to calculate index in buffer
    private int getIndex( int i ){
        return (int)( ((currentPlaying%BUFFSIZE) + i + 1)%BUFFSIZE );
    }

    public long getUnordered(){     //Get unorder packets count
        return unorderedPacket;
    }

    public void resetStats(){       //Method to reset statistical datas'
        unorderedPacket = 0;
        packetLoss = 0;
        intervalPackets = 0;
        prevPacket = currReceivedSeq;
    }

    public long getPacketLoss(){    //Get lossed packets count
        packetLoss = ( currReceivedSeq - prevPacket ) - intervalPackets;
        if ( packetLoss < 0 ) return 0;
        return packetLoss;
    }

    public void run (){

        while(true){        //Continuously play the packets in the buffer
            DataPacket dataPacket = getAudio(); //Get a packet from buffer
            if( dataPacket != null ){           //Play the packet if it is not null
                currentPlaying = dataPacket.getSequenceNo();
                audioObj.playAudio( dataPacket.getVoice() );
            }

            if( (long)System.currentTimeMillis() > startTime + 10000 ){     //In 60s interval print the statistics
                System.out.println("Received Packets: "+ intervalPackets +"| PacketLoss: " + getPacketLoss() + " | Unorderd Packets: " + getUnordered() );
                startTime = System.currentTimeMillis();
                resetStats();
            }
        }

    }

}