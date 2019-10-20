import static org.junit.Assert.*;
import org.junit.Test;
import junit.framework.TestCase;

public class DataPacketTest extends TestCase{
    
    @Test
    public void testObjectToByteArray() {

        System.out.println("ObjectToByteArray method test: ");
        DataPacket instance = null;
        DataPacket rebuild = null;

        byte[] byteArray="Sample Testing Voice".getBytes();
        byte[] result = null;

        instance = new DataPacket( byteArray, 1000, 1);

        try{
			result = DataPacket.ObjectToByteArray( instance );    //Method Output
            rebuild = DataPacket.ByteArrayToObject( result );
		}catch(Exception e){
			System.out.println(e);
		}
		
        assertEquals( instance, rebuild );
    }

    @Test
    public void testReadObject(){

        System.out.println("ByteArrayToObject method test: ");
        byte[] byteArray = "Sample Testing Voice".getBytes();
        byte[] result = null;

		DataPacket instance = new DataPacket( byteArray, 20000, 2 );	
        DataPacket rebuild = null;
        
		try{
			result = DataPacket.ObjectToByteArray( instance );
			rebuild = DataPacket.ByteArrayToObject( result );
		}catch(Exception e){
			System.out.println(e);
        }
        
		assertArrayEquals( instance.getVoice() , rebuild.getVoice()  );
    }

    @Test
    public void testGetSequenceNo() {

        System.out.println("getSequenceNo method test:");
        DataPacket instance = new DataPacket( "Sample Testing Voice".getBytes(), 30000, 3 );

        long expResult = 30000;
        long result = instance.getSequenceNo();

        assertEquals( expResult, result );
    }

    @Test
    public void testSetSequenceNo() {

        System.out.println("setSequenceNo method test:");
        DataPacket instance = new DataPacket( "Sample Testing Voice".getBytes(), 40000, 4 );

        long expResult = 50000;
        instance.setSequenceNo( expResult );
        long result = instance.getSequenceNo();

        assertEquals( expResult, result );
    }

    @Test
    public void testGetVoice(){

        System.out.println("getVoice method test:");
        byte[] expResult =  "Sample Testing Voice".getBytes();
        DataPacket instance = new DataPacket( expResult, 50000, 5);

        byte[] result = instance.getVoice();
        assertArrayEquals( expResult, result );
    }

    @Test
    public void testGetId() {

        System.out.println("getId method test:");
        int expResult = 6;
        DataPacket instance = new DataPacket( "Sample Testing Voice".getBytes(), 60000, expResult );

        int result = instance.getId();

        assertEquals( expResult, result );
    }

}
