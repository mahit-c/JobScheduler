import java.net.*;
import java.io.*;
import java.util.Arrays;

class MyClient {
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
    
       Boolean flag = true;  //For getting largest server info only ONCE
       String largestServerType = null; //For storing the largest server type name
       int largestCoreCount = 0; //For storing the server's core counts to determine the highest (largest) one
       int largestServerCount = 1; //For keeping track of the no.of servers of the largest type
       int serverID = 0; //For scheduling jobs to the required servers
        
     
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
                  int jobID = Integer.parseInt(jobInfo[2]); //job ID is the only important value needed for scheduling
             
			//Check server info only ONCE:
			if (flag) {
			 out.println("GETS All"); //Get server state information
			 sreply=in.readLine(); //Should respond with DATA X Y
			 out.println("OK"); //Send OK 
			 
			 String[] parts = sreply.split("\\s+"); //Storing DATA X Y response to determine nRecs and recSize
		         int nRecs = Integer.parseInt(parts[1]); 
		         int recSize = Integer.parseInt(parts[2]);
               
				for (int i = 0; i < nRecs; i++) {
			     
				    sreply = in.readLine(); //Receiving each record
				    String[] serverInfo = sreply.split("\\s+");
				    String serverType = serverInfo[0];
				    int coreCount = Integer.parseInt(serverInfo[4]);
				    
				     if (serverType.equals(largestServerType)) { //Checking if there is more of the largest server type
				    		largestServerCount++; //Incrementing count if current server type equals to largest
				     } 
                   
				     if (coreCount > largestCoreCount){ 	//Checking if there is a larger server type
				    	 largestServerType = serverType; //Updating largest server type 
				         largestCoreCount= coreCount; //Updating largest core count value
				         largestServerCount=1; // Resetting count 1 if new larger count is found
				     }   
	     		         }
	     
	    		  out.println("OK"); //Send OK
	                  sreply= in.readLine();
	
		       }
	
		flag=false; //Determining largest server only ONCE
	
		if (serverID >= largestServerCount){
			serverID=0; //resetting to 0
		}
	
		//Scheduling jobs:
		out.println("SCHD " + jobID + " "+ largestServerType + " "+ serverID);
		
		//System.out.println("Job assigned to: " + serverID); //Checking job assigment and server ID's
		
		serverID++; //Incrementing server ID to assign next job to next server
		sreply= in.readLine();
		
	     }
	     
	  }
      
        out.println("QUIT"); // Send QUIT to server
    
        sreply = in.readLine(); // Receive QUIT from server

        in.close();
        out.close();
        s.close();
        
     }
   
    
}
  
    
    
  



