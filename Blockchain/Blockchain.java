/* Import all needed libraries. Do not mind the potential errors on google.gson imports, they will not cause
    any issues upon correct compilation */
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.*;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.StringReader;
import java.io.BufferedReader;
import java.text.*;

//Set up our BlockRecord skeleton for unverified blocks
class BlockRecord{
    String BlockID;
    String TimeStamp;
    String VerificationProcessID;
    String PreviousHash; 
    UUID uuid; 
    String Fname;
    String Lname;
    String SSNum;
    String DOB;
    String RandomSeed; 
    String WinningHash;
    String Diag;
    String Treat;
    String Rx;
    
    //Methods to get or set data. Need this in order to grab data down the line. 
    public String getBlockID() {return BlockID;}
    public void setBlockID(String BID){this.BlockID = BID;}
  
    public String getTimeStamp() {return TimeStamp;}
    public void setTimeStamp(String TS){this.TimeStamp = TS;}
  
    public String getVerificationProcessID() {return VerificationProcessID;}
    public void setVerificationProcessID(String VID){this.VerificationProcessID = VID;}
    
    public String getPreviousHash() {return this.PreviousHash;}
    public void setPreviousHash (String PH){this.PreviousHash = PH;}
    
    public UUID getUUID() {return uuid;}
    public void setUUID (UUID ud){this.uuid = ud;}
  
    public String getLname() {return Lname;}
    public void setLname (String LN){this.Lname = LN;}
    
    public String getFname() {return Fname;}
    public void setFname (String FN){this.Fname = FN;}
    
    public String getSSNum() {return SSNum;}
    public void setSSNum (String SS){this.SSNum = SS;}
    
    public String getDOB() {return DOB;}
    public void setDOB (String RS){this.DOB = RS;}
  
    public String getDiag() {return Diag;}
    public void setDiag (String D){this.Diag = D;}

    public String getTreat() {return Treat;}
    public void setTreat (String Tr){this.Treat = Tr;}
  
    public String getRx() {return Rx;}
    public void setRx (String Rx){this.Rx = Rx;}
  
    public String getRandomSeed() {return RandomSeed;}
    public void setRandomSeed (String RS){this.RandomSeed = RS;}
    
    public String getWinningHash() {return WinningHash;}
    public void setWinningHash (String WH){this.WinningHash = WH;}
}

class Blockchain{

  private static String FILENAME;

  Queue<BlockRecord> ourPriorityQueue = new PriorityQueue<>(4, BlockTSComparator);

  //How we organize the data coming in 
  private static final int iFNAME = 0;
  private static final int iLNAME = 1;
  private static final int iDOB = 2;
  private static final int iSSNUM = 3;
  private static final int iDIAG = 4;
  private static final int iTREAT = 5;
  private static final int iRX = 6;

  public static void main(String argv[]) {
    Blockchain b = new Blockchain(argv);
    b.run(argv);
  }

  public static Comparator<BlockRecord> BlockTSComparator = new Comparator<BlockRecord>()
    {
     @Override
     public int compare(BlockRecord b1, BlockRecord b2)
     {
      String s1 = b1.getTimeStamp();
      String s2 = b2.getTimeStamp();
      if (s1 == s2) {return 0;}
      if (s1 == null) {return -1;}
      if (s2 == null) {return 1;}
      return s1.compareTo(s2);
     }
    };

  
  public Blockchain(String argv[]) {
    System.out.println("Welcome to my simple blockchain program");
    System.out.println();
  }
  
  public void run(String argv[]) {
    
    System.out.println("Running now\n");
    try{
      ListExample(argv);
    } catch (Exception x) {};
  }

  public void ListExample(String args[]) throws Exception {
  
     LinkedList<BlockRecord> recordList = new LinkedList<BlockRecord>();

    int pnum;
    int UnverifiedBlockPort;
    int BlockChainPort;

    //If we get more than one argument, something sketchy is going on 
    if (args.length > 1) System.out.println("Special functionality is present \n");

    //Otherwise, match up the argument with the correct process number. If none is given, read in data from BlockInput0.txt
    if (args.length < 1) pnum = 0;
    else if (args[0].equals("0")) pnum = 0;
    else if (args[0].equals("1")) pnum = 1;
    else if (args[0].equals("2")) pnum = 2;
    else pnum = 0; 
    UnverifiedBlockPort = 4710 + pnum;
    BlockChainPort = 4820 + pnum;
    
    System.out.println("Process number: " + pnum + " Ports: " + UnverifiedBlockPort + " " + 
		       BlockChainPort + "\n");

    switch(pnum){
    case 1: FILENAME = "BlockInput1.txt"; break;
    case 2: FILENAME = "BlockInput2.txt"; break;
    default: FILENAME= "BlockInput0.txt"; break;
    }

    System.out.println("Using input file: " + FILENAME);

    //Create a dummy node and add it to the list of unverified blocks so we can later loop through and grab it's "previous hash"
    BlockRecord dummyNode = new BlockRecord();
    dummyNode.setBlockID("dummy_node");
    recordList.add(dummyNode);


    try {
      BufferedReader br = new BufferedReader(new FileReader(FILENAME));
      String[] tokens = new String[10];
      String InputLineStr;
      String suuid;
      UUID idA;
      BlockRecord tempRec;
      
      StringWriter sw = new StringWriter();
      
      int n = 0;
      System.out.println();
      while ((InputLineStr = br.readLine()) != null) {
	    BlockRecord BR = new BlockRecord(); 

	    try{Thread.sleep(1001);}catch(InterruptedException e){}
          	Date date = new Date();
	    //helps build the correct timestamp
	    String T1 = String.format("%1$s %2$tF.%2$tT", "", date);
	    String TimeStampString = T1 + "." + pnum; 
	    System.out.println("Timestamp: " + TimeStampString);
	    BR.setTimeStamp(TimeStampString); 
        
	    suuid = new String(UUID.randomUUID().toString());
	    BR.setBlockID(suuid);
	    //But data into the BlockRecord format we have set up
	    tokens = InputLineStr.split(" +");
	    BR.setFname(tokens[iFNAME]);
	    BR.setLname(tokens[iLNAME]);
	    BR.setSSNum(tokens[iSSNUM]);
	    BR.setDOB(tokens[iDOB]);
	    BR.setDiag(tokens[iDIAG]);
	    BR.setTreat(tokens[iTREAT]);
	    BR.setRx(tokens[iRX]);
        
	    recordList.add(BR);
	    n++;
          }
    } catch (Exception e) {e.printStackTrace();}

    System.out.println();

    //DO WORK ON BLOCKCHAIN HERE. Returns a linked list of verified blocks
    LinkedList verified_blocks = doWork(recordList);
    
    //Make a new instance of GSON.
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    // Convert the Java object to a JSON String:
    String json = gson.toJson(verified_blocks);
    
    // Write the JSON data we have to an external file.
    try (FileWriter writer = new FileWriter("BlockchainLedger.json")) {
      gson.toJson(recordList, writer);
    } catch (IOException e) {e.printStackTrace();}
}


//Function to do work on unverified blocks
public static LinkedList doWork(LinkedList<?> recordList){
    //Make a new empty linked list of verified blocks. We will add to this later.
    LinkedList verified_blocks = new LinkedList<>();
    //Make a variable holding the previous proof of work string so we can concatenate it. Will will update this in the loop.
    String previous_proof_of_work = "halsdkfjal";
    //Instantiate an iterator so we can navagate the linkedlist of unverified blocks
    Iterator<BlockRecord> iterator = (Iterator<BlockRecord>) recordList.iterator();
    //Iterate through the unverified blocks
    while(iterator.hasNext()){
        BlockRecord tempNode = iterator.next();
        MessageDigest MD;
        int work_num = 0;
        System.out.println("Unverified block: " + tempNode);
        try {
            while (true){
                Thread.sleep(100);
                String RandString = randomAlphaNumeric(8);
                //To do proper work, we need to concatenate block data, previous proof of work string, and a random seed
                String concatString = tempNode.getBlockID()+RandString+previous_proof_of_work;
                MD = MessageDigest.getInstance("SHA-256");
                byte[] bytesHash = MD.digest(concatString.getBytes("UTF-8"));
                String hash = ByteArrayToString(bytesHash);
                System.out.println("Hash: " + hash);
                work_num = Integer.parseInt(hash.substring(0,4), 16);
                System.out.println("First 16 bits in decimal: " + work_num);
                //If the work_num is less than 20,000, we have sucessfully solved the puzzle. If not, keep going. 
                if (work_num < 20000){
                    System.out.print("Block record: ");
                    System.out.print(tempNode);
                    System.out.println(" has been solved!");
                    System.out.println("Proof of work: " + RandString);
                    System.out.println();
                    previous_proof_of_work = RandString;
                    tempNode.setPreviousHash(previous_proof_of_work);
                    //If the puzzle was solved, add the node to the linked list of verified blocks.
                    verified_blocks.add(tempNode);
                    break;
                }else{
                    //If not, keep going. This usually takes a while in real life.
                    System.out.println(work_num + " is not less than 20,000. Keep trying!");
                }
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    System.out.println("Verfified blocks: ");
    System.out.println(verified_blocks);
    //Make sure to return the verified blocks
    return verified_blocks;
}
//Helper function to Make a random string
public static String randomAlphaNumeric(int count) {
    String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	StringBuilder builder = new StringBuilder();
	while (count-- != 0) {
	    int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
	    builder.append(ALPHA_NUMERIC_STRING.charAt(character));
	}
	return builder.toString();
    }
//Helper function to assist in hashing
public static String ByteArrayToString(byte[] ba){
    StringBuilder hex = new StringBuilder(ba.length * 2);
    for(int i=0; i < ba.length; i++){
        hex.append(String.format("%02X", ba[i]));
    }
    return hex.toString();
    }
}