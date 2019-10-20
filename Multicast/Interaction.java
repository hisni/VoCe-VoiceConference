import java.util.Scanner; 

public class Interaction extends Thread{

    private static int currState = 0;
    
    public Interaction( int state ){
        currState = state;
    }

    public static int getCurrState(){
        return currState;
    }

    public void run(){
        Scanner scanner = new Scanner( System.in );
        try{
            while( true ){
                if( currState == 0 ){
                    System.out.println( "Enter | 'T' to talk | 'E' to stop | 'exit' to Close |");
                }
                String readString = scanner.nextLine();

                if( readString.equalsIgnoreCase("T") & currState == 0 ){
                    currState = 1;
                    System.out.println("Recoding & Sending");
                }else if( readString.equalsIgnoreCase("E") & currState == 1 ) {
                    currState = 0;
                    System.out.println("Recoding Stoped");
                }else if( readString.equalsIgnoreCase("exit") ){
                    System.exit(0);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            scanner.close();
        }
    }
}