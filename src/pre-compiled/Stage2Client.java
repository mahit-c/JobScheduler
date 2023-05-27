import java.net.*;
import java.io.*;
import java.util.Arrays;

class Stage2Client {
    public static void main(String args[]) throws Exception {
    
    	//Initial connection and communication establishment:
       Socket s = new Socket("127.0.0.1", 50000);
       BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
       PrintWriter out = new PrintWriter(s.getOutputStream(), true);
       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

       out.println("HELO"); // Client sends HELO
       String sreply = in.readLine(); //Storing server responses //Server should respond with OK here
	
       String username = "mahit";
       String authMessage = "AUTH " + username;
       out.println(authMessage); // Client sends AUTH username
 
       sreply = in.readLine(); //Server should respond OK
    
       //Boolean flag = true;  //For getting largest server info only ONCE
       //String largestServerType = null; //For storing the largest server type name
       //int largestCoreCount = 0; //For storing the server's core counts to determine the highest (largest) one
       //int largestServerCount = 1; //For keeping track of the no.of servers of the largest type
       //int serverID = 0; //For scheduling jobs to the required servers
        
    	String serverToSend = null;
    	String serverID=null;
     
       while (true) {
      
         out.println("REDY"); // Send REDY to request for job
         sreply = in.readLine(); // Receive a message
        
      		if (sreply.equals("NONE")){ //Break out of the loop if no jobs available
		   break;
		}
	
		else if (sreply.startsWith("JCPL")) { //Next iteration of loop after job completion message
		  continue;
		}
	
		else { //Server response is JOBN; Start scheduling process + determine largest server (only first loop iteration)
	
		//Store job details:
		  String[] jobInfo = sreply.split("\\s+");
		  System.out.println("JOBINFO ARRAY: " + jobInfo[0]);
                  int jobID = Integer.parseInt(jobInfo[2]); //job ID is the only important value needed for scheduling
                  int core = Integer.parseInt(jobInfo[4]);
                  int memory = Integer.parseInt(jobInfo[5]);
                  int disk = Integer.parseInt(jobInfo[6]);
                  
                  System.out.println("Job Details are:" + core + memory + disk);
                  
             //Check currently available servers using GETS Avail:
             
			 out.println("GETS Avail " + core + " "+ memory + " "+ disk); 
			 sreply=in.readLine(); //Should respond with DATA X Y
			 
			
			 System.out.println("Server response to GETS AVAIL:" + sreply);
                  
			 
			 //
			 out.println("OK"); //Send OK 
			 
			 String[] parts = sreply.split("\\s+"); //Storing DATA X Y response to determine nRecs and recSize
			
                  
		         int nRecs = Integer.parseInt(parts[1]); 
		         int recSize = Integer.parseInt(parts[2]);
		         
		          if (nRecs==0) { //If NO Available servers go to servers that have jobs waiting
		          	
		      	
			 	System.out.println("Server responded 0");
			 	sreply=in.readLine();
			 	System.out.println("First reply inside the if = " + sreply);
			 	
			 	//System.out.println("Server reply after DATAXY OK = " + sreply);
			 	out.println("OK"); //Send OK 
			 	sreply=in.readLine(); 
			 	System.out.println("Server reply after OK = " + sreply); //Should be . here
	
			 
			 	out.println("GETS Capable " + core + " "+ memory + " "+ disk); 
			 	
			 	System.out.println("GETS Capable " + core + " "+ memory + " "+ disk);  //Checking for CAPABLE SERVERS (which can eventually do it);
			 	
			 	sreply = in.readLine();
			 	sreply = in.readLine();
			 	System.out.println( "Reply after GETS Capable: " + sreply);
			 	
			 	out.println("OK"); //Send OK after DATA X Y
			 	
			 	String [] newParts = sreply.split("\\s+");
			 	nRecs = Integer.parseInt(newParts[1]); //Updating value of nRecs
			 	System.out.println("New Value of NRECS: " + nRecs);
			 	
			 } 
		         
               //Scheduling job to First AVAILABLE SERVER:
				
				for (int i=0; i<nRecs; i++) {
			     
				    sreply = in.readLine(); //Receiving each record
				    System.out.println("SERVER INFO REPLY: " + sreply);
				    String[] serverInfo = sreply.split("\\s+");
				    
				    if (i==0) {
				    serverToSend = serverInfo[0];
				    serverID = serverInfo[1];
				    }
				    
				   }
				    
                   

	     
	    		out.println("OK"); //Send OK
	                sreply= in.readLine();
	
		       
	
		
	
		//Scheduling jobs:
		out.println("SCHD " + jobID + " "+ serverToSend + " "+ serverID);
		
		System.out.println("SCHD " + jobID + " "+ serverToSend + " "+ serverID); //Checking job assigment and server ID's
		
		sreply= in.readLine();
		
		System.out.println("Server response after SCHD " + sreply);
		
		//}
		
	     }
	     
	  }
	  
	
      
        out.println("QUIT"); // Send QUIT to server
    
        sreply = in.readLine(); // Receive QUIT from server

        in.close();
        out.close();
        s.close();
        
     }
     
    }
    
   
   
    

  
    
    
  



